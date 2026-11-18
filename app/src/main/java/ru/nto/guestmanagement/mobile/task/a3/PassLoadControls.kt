package ru.nto.guestmanagement.mobile.task.a3

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import ru.nto.guestmanagement.mobile.R
import ru.nto.guestmanagement.mobile.model.PassScreenState
import ru.nto.guestmanagement.mobile.ui.TestIds

@Composable
fun PassLoadControls(
    state: PassScreenState,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        when (state) {
            is PassScreenState.InitialLoading -> {
                CircularProgressIndicator(modifier = Modifier.testTag(TestIds.PASS_LOADING))
            }

            is PassScreenState.InitialError -> {
                Text(
                    text = state.message,
                    modifier = Modifier.testTag(TestIds.PASS_ERROR),
                )
                Button(
                    onClick = onRetry,
                    modifier = Modifier.testTag(TestIds.PASS_RETRY),
                ) {
                    Text(stringResource(R.string.action_retry))
                }
            }

            is PassScreenState.Content -> {
                Button(
                    onClick = onRefresh,
                    enabled = !state.isRefreshing,
                    modifier = Modifier.testTag(TestIds.PASS_REFRESH),
                ) {
                    Text(stringResource(R.string.action_refresh))
                }

                if (state.isRefreshing) {
                    CircularProgressIndicator(modifier = Modifier.testTag(TestIds.PASS_LOADING))
                } else if (state.isOffline) {
                    Text(
                        text = stringResource(R.string.offline),
                        modifier = Modifier.testTag(TestIds.PASS_OFFLINE),
                    )
                }
            }
        }
    }
}
