package ru.nto.guestmanagement.mobile.task.a2

import ru.nto.guestmanagement.mobile.model.PassDetails
import ru.nto.guestmanagement.mobile.model.PassStatus
import ru.nto.guestmanagement.mobile.model.Scan
import ru.nto.guestmanagement.mobile.model.ScanDirection
import java.time.LocalDateTime

/** A2. Чистая функция: статус пропуска на момент [now] по правилу из технического задания. */
fun calculatePassStatus(pass: PassDetails, scans: List<Scan>, now: LocalDateTime): PassStatus {
    if (pass.revokedAt != null) return PassStatus.BLOCKED
    if (now < pass.validFrom) return PassStatus.NOT_STARTED
    if (now > pass.validTo) return PassStatus.EXPIRED

    val entriesUsed = scans.count { scan ->
        scan.direction == ScanDirection.ENTRY && scan.scannedAt <= now
    }
    return if (entriesUsed >= pass.maxEntries) PassStatus.EXHAUSTED else PassStatus.ACTIVE
}
