/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.asynctaskloader;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.asynctaskloader.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = "MainActivity";

    /* A constant to save and restore the URL that is being displayed */
    private static final String SEARCH_QUERY_URL_EXTRA = "query";

    public static final int GITHUB_SEARCH_LOADER = 12345;

    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;
    private TextView mSearchResultsTextView;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);

        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        if (savedInstanceState != null) {
            String queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA);

            mUrlDisplayTextView.setText(queryUrl);
        }
        getLoaderManager().initLoader(GITHUB_SEARCH_LOADER, null, this);
    }

    /**
     * This method retrieves the search text from the EditText, constructs the
     * URL (using {@link NetworkUtils}) for the github repository you'd like to find, displays
     * that URL in a TextView, and finally request that an AsyncTaskLoader performs the GET request.
     */
    private void makeGithubSearchQuery() {
        String githubQuery = mSearchBoxEditText.getText().toString();

        if (githubQuery == null || githubQuery.isEmpty()) {
            return;
        }

        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(githubSearchUrl.toString());

        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_QUERY_URL_EXTRA, String.valueOf(githubSearchUrl));

        LoaderManager loaderManager = getLoaderManager();
        Loader<String> loader = loaderManager.getLoader(GITHUB_SEARCH_LOADER);
        if (loader == null) {
            Log.d(TAG, "makeGithubSearchQuery: Init Loader");
            loaderManager.initLoader(GITHUB_SEARCH_LOADER, bundle, this);
        } else {
            Log.d(TAG, "makeGithubSearchQuery: Restart Loader");
            loaderManager.restartLoader(GITHUB_SEARCH_LOADER, bundle, this);
        }

    }

    /**
     * This method will make the View for the JSON data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showJsonDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the JSON data is visible */
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the JSON
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<String> onCreateLoader(int i, final Bundle bundle) {
        Log.d(TAG, "onCreateLoader: ");
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                Log.d(TAG, "onStartLoading: ");
                super.onStartLoading();
                if (bundle == null) {
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                Log.d(TAG, "loadInBackground: ");
                String githubSearchResults = null;
                try {
                    URL searchUrl = new URL(bundle.getString(SEARCH_QUERY_URL_EXTRA));
                    githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return githubSearchResults;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        Log.d(TAG, "onLoadFinished: Called");
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (s != null && !s.equals("")) {
            showJsonDataView();
            mSearchResultsTextView.setText(s);
        } else {
            Log.e(TAG, "onLoadFinished: " + String.valueOf(s == null));
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String queryUrl = mUrlDisplayTextView.getText().toString();
        outState.putString(SEARCH_QUERY_URL_EXTRA, queryUrl);

    }
}