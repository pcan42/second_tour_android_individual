package ru.nto.guestmanagement.mobile.task.a2

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import ru.nto.guestmanagement.mobile.R
import ru.nto.guestmanagement.mobile.data.TimeProvider
import ru.nto.guestmanagement.mobile.model.PassData
import ru.nto.guestmanagement.mobile.model.PassStatus
import ru.nto.guestmanagement.mobile.ui.PassQr
import ru.nto.guestmanagement.mobile.ui.TestIds
import ru.nto.guestmanagement.mobile.ui.statusColor

@Composable
fun PassAccessPanel(data: PassData, timeProvider: TimeProvider, modifier: Modifier = Modifier) {
    val now = timeProvider.now()
    val status = calculatePassStatus(data.pass, data.scans, now)

    val statusText = when (status) {
        PassStatus.ACTIVE -> stringResource(R.string.pass_status_active)
        PassStatus.NOT_STARTED -> stringResource(R.string.pass_status_not_started)
        PassStatus.EXPIRED -> stringResource(R.string.pass_status_expired)
        PassStatus.EXHAUSTED -> stringResource(R.string.pass_status_exhausted)
        PassStatus.BLOCKED -> stringResource(R.string.pass_status_blocked)
    }
    val statusColor = when (status) {
        PassStatus.ACTIVE -> colorResource(R.color.status_active)
        PassStatus.NOT_STARTED -> colorResource(R.color.status_not_started)
        PassStatus.EXPIRED -> colorResource(R.color.status_expired)
        PassStatus.EXHAUSTED -> colorResource(R.color.status_exhausted)
        PassStatus.BLOCKED -> colorResource(R.color.status_blocked)
    }

    Column(modifier = modifier) {
        Text(
            text = statusText,
            color = statusColor,
            modifier = Modifier
                .testTag(TestIds.PASS_STATUS)
                .statusColor(statusColor),
        )

        if (status == PassStatus.ACTIVE) {
            PassQr(data.pass.qrCode)
        } else {
            Text(
                text = stringResource(R.string.qr_unavailable),
                modifier = Modifier.testTag(TestIds.PASS_QR_HIDDEN),
            )
        }
    }
}
