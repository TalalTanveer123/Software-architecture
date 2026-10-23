# Depot Manager — Parcel Collection System
## Software Architecture Assignment — Part 2

---

## Project Structure

```
DepotManager/
├── src/
│   ├── app/
│   │   └── DepotManagerApp.java          ← Entry point (main)
│   ├── controller/
│   │   ├── DepotController.java          ← MVC Controller + DepotListener interface
│   │   └── Worker.java                   ← Fee calculation & customer processing
│   ├── model/
│   │   ├── Customer.java                 ← Domain: customer
│   │   ├── CustomerQueue.java            ← Container: FIFO queue (LinkedList)
│   │   ├── InvalidParcelDataException.java ← Checked exception
│   │   ├── Log.java                      ← Singleton event log
│   │   ├── Parcel.java                   ← Domain: parcel with validation
│   │   ├── ParcelMap.java                ← Container: parcel store (HashMap)
│   │   └── ProcessingResult.java         ← Immutable value object
│   ├── util/
│   │   ├── DataLoader.java               ← CSV file reader
│   │   └── ReportWriter.java             ← End-of-day report writer
│   └── view/
│       ├── ControlPanel.java             ← Action buttons
│       ├── CustomerQueuePanel.java       ← Queue display
│       ├── DepotManagerFrame.java        ← Main JFrame
│       ├── ParcelListPanel.java          ← Parcel awaiting collection display
│       └── ProcessingPanel.java          ← Current transaction display
├── test/
│   ├── ParcelTest.java
│   ├── CustomerTest.java
│   ├── ParcelMapTest.java
│   ├── CustomerQueueTest.java
│   ├── LogTest.java
│   ├── ProcessingResultTest.java
│   ├── WorkerTest.java
│   ├── DataLoaderTest.java
│   └── DepotControllerTest.java
├── data/
│   ├── parcels.txt                       ← 15 sample parcels
│   └── customers.txt                     ← 15 sample customers
├── lib/                                  ← JUnit 5 jar goes here (see below)
├── build.ps1                             ← PowerShell build + test + run script
├── .classpath                            ← Eclipse project file
├── .project                              ← Eclipse project file
├── DepotManager.iml                      ← IntelliJ IDEA module file
└── .gitignore
```

---

## How to Open in Eclipse

1. Open Eclipse → **File → Import → Existing Projects into Workspace**
2. Browse to `e:\Software Architecture\DepotManager` → Finish
3. JUnit 5 is built into Eclipse — no extra jars needed
4. Right-click the `test` folder → **Run As → JUnit Test** to run all tests
5. Right-click `DepotManagerApp.java` → **Run As → Java Application** to launch the GUI

---

## How to Open in IntelliJ IDEA

1. **File → Open** → select `e:\Software Architecture\DepotManager`
2. IntelliJ will detect the `.iml` file and configure the module
3. Download the JUnit 5 standalone jar (see below) and place it in `lib/`
4. Right-click the `test` folder → **Run 'All Tests'**
5. Right-click `DepotManagerApp.java` → **Run 'DepotManagerApp.main()'**

### Getting the JUnit 5 jar (IntelliJ only)

Download from Maven Central and save as `lib/junit-platform-console-standalone.jar`:
```
https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar
```

---

## How to Build and Run from Command Line (PowerShell)

```powershell
cd "e:\Software Architecture\DepotManager"

# Download JUnit 5 jar automatically, compile, run tests, launch app:
.\build.ps1
```

Or manually:

```powershell
# 1. Create output directory
New-Item -ItemType Directory -Force -Path bin

# 2. Compile all source files
$srcFiles = Get-ChildItem -Recurse -Path src -Filter "*.java" | % { $_.FullName }
javac -d bin ($srcFiles -join " ")

# 3. Run the application
java -cp bin app.DepotManagerApp
```

---

## Running the Tests Manually

```powershell
# After compiling sources (step 1+2 above):
$JUNIT = "lib\junit-platform-console-standalone.jar"

# Compile tests
$testFiles = Get-ChildItem -Recurse -Path test -Filter "*.java" | % { $_.FullName }
javac -d bin -cp "bin;$JUNIT" ($testFiles -join " ")

# Run all tests
java -cp "bin;$JUNIT" org.junit.platform.console.standalone.ConsoleLauncher --scan-classpath --classpath=bin
```

---

## Using the GUI

Once the application launches you will see a **dark-themed Swing window** with:

| Panel | What it shows |
|---|---|
| **Parcels Awaiting Collection** (top-left) | Table of all uncollected parcels with ID, weight, volume, days |
| **Customer Queue** (top-right) | FIFO queue of waiting customers |
| **Current Processing** (centre) | Customer name, parcel details, fee for the last transaction |
| **Control Buttons** (bottom bar) | Four action buttons |

### Buttons

| Button | Action |
|---|---|
| ▶ Process Next Customer | Dequeues the front customer, finds their parcel, calculates fee, marks collected |
| ➕ Add Parcel | Opens a dialog to add a new parcel to the depot |
| 👤 Add Customer | Opens a dialog to add a new customer to the queue |
| 🌙 End Day | Prompts for a save location and writes the end-of-day report + event log |

All panels refresh **instantly** whenever the model changes (Observer pattern).

---

## Git Log Instructions

After setting up, initialise git and commit incrementally:

```powershell
cd "e:\Software Architecture\DepotManager"
git init
git config user.name "Your Name"
git config user.email "your@email.com"

# Commit 1
git add src/model/Parcel.java src/model/InvalidParcelDataException.java
git commit -m "Implement Parcel constructor validation; all ParcelTest tests pass"

# Commit 2
git add src/model/Customer.java
git commit -m "Implement Customer constructor validation; all CustomerTest tests pass"

# Commit 3
git add src/model/ParcelMap.java
git commit -m "Implement ParcelMap (HashMap); all ParcelMapTest tests pass"

# Commit 4
git add src/model/CustomerQueue.java
git commit -m "Implement CustomerQueue (LinkedList FIFO); all CustomerQueueTest tests pass"

# Commit 5
git add src/model/Log.java
git commit -m "Implement Log singleton with StringBuffer; all LogTest tests pass"

# Commit 6
git add src/model/ProcessingResult.java
git commit -m "Implement ProcessingResult immutable value class; all ProcessingResultTest pass"

# Commit 7
git add src/controller/Worker.java
git commit -m "Implement Worker fee formula and processNextCustomer; all WorkerTest pass"

# Commit 8
git add src/util/DataLoader.java
git commit -m "Implement DataLoader CSV parser; all DataLoaderTest tests pass"

# Commit 9
git add src/util/ReportWriter.java
git commit -m "Implement ReportWriter end-of-day report"

# Commit 10
git add src/controller/DepotController.java
git commit -m "Implement DepotController MVC with Observer (DepotListener); all DepotControllerTest pass"

# Commit 11
git add src/view/
git commit -m "Implement Swing GUI: ParcelListPanel, CustomerQueuePanel, ProcessingPanel, ControlPanel, DepotManagerFrame"

# Commit 12
git add src/app/DepotManagerApp.java data/
git commit -m "Implement DepotManagerApp entry point; load sample data; full integration working"
```

---

## Design Patterns Used

| Pattern | Where |
|---|---|
| **Singleton** | `Log.java` — private constructor, `getInstance()` only access point |
| **MVC** | Model: `model` package; Controller: `DepotController`; View: `view` package |
| **Observer** | `DepotController.DepotListener` interface; views call `addDepotListener()` |

**DepotController never imports anything from `view`** — the Observer interface ensures one-way dependency.
