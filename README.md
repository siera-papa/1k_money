# 1K Money v0.4.5

A small native Android money ledger built around fast home-screen entry and a read-only 1K Dashboard integration path.

## Current scope

- Expense / Income / Transfer
- Multi-currency accounts (RSD, EUR, USD, CNY, BTC)
- Income sources
- Two-level categories (category + subcategory)
- 0-N tags per transaction
- Edit/delete transaction history
- Separate 1x1 Expense / Transfer / Income widgets
- Dark UI and translucent quick-entry overlay
- Last transaction configuration reused as the next default
- Read-only ContentProvider for the future 1K Dashboard

## Local backup & restore

Settings now includes a local backup section.

- Choose a local folder once through Android's Storage Access Framework.
- The folder permission persists across app and phone restarts.
- A first backup is created immediately after choosing the folder.
- WorkManager creates an automatic backup roughly once per day.
- The latest 14 automatic backups are retained.
- `Backup now` creates a timestamped manual backup that is not auto-deleted.
- `Restore from backup file` restores all Money data from a selected JSON backup.
- Before restore, if a backup folder is configured, the app creates a timestamped pre-restore safety backup.
- Restore validates IDs/references before replacing current data and applies the replacement in one Room transaction.

Backup files contain accounts, opening balances, income sources, categories/subcategories, tags, transactions, transaction legs and transaction-tag links. 1Money import/export is intentionally not included.

## Build

```bash
./gradlew clean assembleDebug
```

Install/update:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```


## v0.4.2
- Transaction date can be selected in quick entry; new transactions default to the current date/time.
- Accounts have an optional Tracker ID for joining with betting-tracker JSON. Tracker ID is included in backups and the read-only ContentProvider.


## v0.4.2

- Quick-entry date moved below the numeric keypad.
- Calendar button is in the keypad action column between delete and save.
- All user-visible dates use `d MMM yy`, e.g. `4 Okt 26`; date+time uses `4 Okt 26 · 18:25`.


## v0.4.3

- Quick-entry overlay now draws behind the Android navigation/gesture area and sits flush with the physical bottom edge.
- Removed the empty launcher strip below the sheet while keeping the gesture bar usable.


## v0.4.4

- Increased internal bottom padding of the quick-entry sheet from 8 dp to 24 dp so the selected date sits clearly above the Android gesture bar while the sheet background still reaches the physical bottom edge.


## v0.4.5

- Swapped the bottom keypad positions of `0` and `00`: `00` is now left and `0` is right, closer to the save column.

## v0.5.0 — Cash Calc

- Novi `Calc` tab neposredno pre Settings.
- Apoeni: 5000, 2000, 1000, 500, 200, 100, 50, 20, 10 RSD.
- Za svaki apoen: `−`, direktan broj komada i `+`.
- Ukupan keš se računa odmah; nema Save dugmeta i Calc ne menja nijedan Money račun.
- 10/20/50/100/200 čuvaju broj komada lokalno posle svake izmene.
- 500/1000/2000/5000 su samo sesijski i kreću od 0 pri sledećem startu app procesa.
- Novi zaseban 1x1 `Cash Calc` home-screen widget (`123`) otvara isti calculator kao translucent bottom overlay.
