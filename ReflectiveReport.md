# Reflective Report — Part 2 Implementation
## Software Architecture: Depot Manager

---

### How Part 1 Use Cases Map to Part 2 Classes and Methods

My Part 1 use case **Join Collection Queue** is realised in `DepotController.addCustomer(name, parcelId)`, which constructs a new `Customer` with an auto-incremented sequence number and enqueues it via `CustomerQueue.addCustomer()`. Every join event is recorded by calling `Log.getInstance().logEvent()`, fulfilling the «include» Record Event to Log use case I identified in my diagram.

The use case **Process Next Customer** maps directly to `Worker.processNextCustomer(queue, parcelMap)`, which dequeues the front customer, delegates to **Look Up Parcel** (resolved by `ParcelMap.getParcel(id)`), calls **Calculate Collection Fee** via `Worker.calculateFee(parcel)`, and then executes **Release Parcel** by calling `parcel.setCollected(true)`. The whole transaction is wrapped by `DepotController.processNextCustomer()`, which also triggers Observer notifications to all registered views.

**Add New Parcel** maps to `DepotController.addParcel(...)`, which constructs a validated `Parcel` and stores it in `ParcelMap`. The GUI equivalent is the "Add Parcel" dialog in `ControlPanel`.

**Generate End-of-Day Report** is realised in `ReportWriter.writeReport(...)`, called from `ControlPanel` when the worker presses the End Day button.

---

### Differences Between Part 1 Design and Part 2 Fixed Structure

My Part 1 class diagram included a single **Depot** class that combined parcel storage with fee calculation and customer processing. In Part 2 this is split into three classes: `ParcelMap` (storage only), `Worker` (calculation and processing logic), and `DepotController` (coordination). In retrospect this is a cleaner separation of concerns: `ParcelMap` is a pure data structure, `Worker` is a stateless service, and `DepotController` owns application state and observer registration. This aligns more precisely with the Single Responsibility Principle.

I also did not originally model `ProcessingResult` as a separate class. The fixed structure's use of this small immutable value object is an improvement over my Part 1 design, which returned status information through method return codes. Using `ProcessingResult` makes the outcome of each transaction self-describing and trivially testable.

The `DepotController.DepotListener` nested interface formalises the Observer pattern in a way my Part 1 design only implied informally. Implementing it with anonymous classes (as required — no lambdas) makes the dependency direction explicit: views depend on the controller but the controller never references any view class.

---

*Word count: ~290 words*
