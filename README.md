# Smart Contact Management Android Application

## 1. Introduction

Mobile applications play a vital role in managing daily activities efficiently. A Contact Management Application allows users to store, manage, and retrieve contact information using a mobile device.

This project, **Smart Contact Management Android Application (V1)**, is developed as part of the **Mobile Programming (CACS351)** course. Version 1 has been developed to a production-ready state, implementing core Android concepts and a modern, professional user interface based on Google's Material Design principles.

---

## 2. Objectives

The objectives of Version 1 are:

- To develop a fully functional Android application using Android Studio.
- To implement complete CRUD (Create, Read, Update, Delete) operations for contacts.
- To store contact data locally using an SQLite database.
- To display contact data efficiently and elegantly using `RecyclerView` and `MaterialCardView`.
- To provide an intuitive, validation-enabled user interface using Material Design components.
- To perform all database operations asynchronously to ensure a smooth user experience.
- To follow proper Git version control practices.

---

## 3. Scope of Version 1

### Included in V1
- **Offline Contact Storage**: All contacts are saved locally on the device.
- **Full CRUD Functionality**: Add, view, update, and delete contacts.
- **Expanded Data Model**: Contacts include fields for first name, last name, company, phone, and email.
- **Modern UI/UX**: A polished interface using `MaterialToolbar`, `MaterialCardView`, and `TextInputLayout`.
- **Contact Detail Screen**: A read-only screen to view contact details before editing.
- **Search Functionality**: Users can filter the contact list in real-time by name or company.
- **Asynchronous Database Operations**: All database interactions are performed on a background thread to prevent UI freezes.
- **Database Schema Migration**: The database version is updated to handle the new data model.

### Excluded from V1
- System phone contacts integration.
- Caller identification and cloud synchronization.

---

## 4. Tools and Technologies Used

| Category | Technology |
|---|---|
| Programming Language | Java |
| IDE | Android Studio |
| Platform | Android |
| Database | SQLite |
| UI Components | Activities, RecyclerView, Material Design Components |
| Concurrency | `java.util.concurrent.Executors` |
| Version Control | Git |
| Minimum SDK | API 21 |

---

## 5. Application Architecture (V1)

Version 1 follows a simple Activity-centric architecture, enhanced with modern UI components and asynchronous data handling.

`Activity` -> `AppExecutor` -> `SQLite Database`
   |             ^      
`RecyclerView` & `Adapter` (UI Thread) 

This architecture reinforces Android fundamentals while introducing best practices for concurrency.

---

## 6. Project Folder Structure

```
SmartContacts/
│
├── app/
│ ├── src/main/
│ │ ├── java/com/example/smartcontacts/
│ │ │ ├── AppExecutor.java
│ │ │ ├── activities/
│ │ │ │ ├── MainActivity.java
│ │ │ │ ├── AddEditContactActivity.java
│ │ │ │ └── ContactDetailActivity.java
│ │ │ ├── adapter/
│ │ │ │ └── ContactAdapter.java
│ │ │ ├── database/
│ │ │ │ └── DBHelper.java
│ │ │ └── model/
│ │ │   └── Contact.java
│ │ ├── res/
│ │ │ ├── drawable/
│ │ │ ├── layout/
│ │ │ ├── menu/
│ │ │ └── values/
│ │ └── AndroidManifest.xml
│
├── .gitignore
└── README.md
```

---

## 7. Git Workflow and Version Control Strategy

### 7.1. Commit Timeline for V1

1.  **Commit 1-8**: Initial setup, CRUD, UI overhaul, expanded model, detail screen.
2.  **Commit 9: Search Functionality**
    ```bash
    git commit -m "feat: implement search and filtering for contacts"
    ```
3.  **Commit 10: Asynchronous Database Operations**
    ```bash
    git commit -m "refactor: move all database operations to background threads"
    ```
4.  **Commit 11: Documentation**
    ```bash
    git commit -m "docs: update project documentation for V1"
    ```

### 7.2. Merge and Tag
```bash
git checkout main
git merge v1-dev
git tag v1.0
```

---

## 8. Implementation Details and Code

### 8.1. Asynchronous Execution (`AppExecutor.java`)

A singleton class was created to manage background and main thread execution.

```java
public class AppExecutor {
    private static AppExecutor instance;
    private final Executor diskIO;
    private final Executor mainThread;

    // ... singleton implementation ...

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }
}
```

### 8.2. Main Activity (`MainActivity.java`)

All database calls were refactored to use the `AppExecutor`.

```java
public class MainActivity extends AppCompatActivity implements ContactAdapter.OnContactClickListener {
    private void loadContacts() {
        AppExecutor.getInstance().diskIO().execute(() -> {
            contacts = dbHelper.getAllContacts();
            AppExecutor.getInstance().mainThread().execute(() -> {
                // ... update adapter on main thread
            });
        });
    }
    // ...
}
```

### 8.3. Add/Edit Contact Activity (`AddEditContactActivity.java`)

Saving and loading contacts are now done asynchronously.

```java
public class AddEditContactActivity extends AppCompatActivity {
    private void saveContact() {
        // ... validation ...
        AppExecutor.getInstance().diskIO().execute(() -> {
            if (contactId == -1) {
                dbHelper.insertContact(contact);
            } else {
                dbHelper.updateContact(contact);
            }
            AppExecutor.getInstance().mainThread().execute(() -> {
                // ... show toast and finish on main thread
            });
        });
    }
    // ...
}
```

---

## 9. Conclusion

Version 1 of the Smart Contact Management application has been successfully developed into a production-ready state. It meets all the defined objectives, including full CRUD functionality, an expanded data model, a polished Material Design UI, and core features like search. The critical implementation of asynchronous database operations ensures a smooth and responsive user experience, making the app robust and professional.

---

## 10. Future Work

- **Version 2**: Implement an "empty state" view and migrate to MVVM architecture.
- **Version 3**: Caller identification, spam detection, and cloud synchronization.
