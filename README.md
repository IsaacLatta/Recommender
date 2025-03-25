# Inked (Recommender)

## Overview
Inked is an Android Studio app that allows users to create reading groups, recommend books, and manage group memberships. Admins can approve or deny book recommendations and promote users to admin status.

## Notes For Our Instructor

- The report, testing report, presentation, and poster can be found in the **Docs** folder.
- The APK debug binary can be found in the **Build** folder.
- The full android app can be found within the **App** folder.
- The backend API can be found in the **Backend** folder.

## Running the App
- The API key and API URL are ignored in this repo but are built into the APK file.
- If the network functionality (i.e., loading users, books, etc.) isn’t working, it is likely that the backend server is offline.
- We will leave the backend server running. Should it shut down for any reason and you need it to test the app, please let us know and we will restart it.
- Some critical configuration files (e.g., `local.properties`, `gradle.properties`) are ignored. If you attempt to build the app on your own, you may need to generate these files and include the API key and API URL in `gradle.properties`. If absolutely necessary, we can provide these.
- The username and password for the account we used to test and demo the application are:
  - **Username:** alice
  - **Password:** password123
- The `start_backend` and `test_server` scripts require credentials loaded from environment variables, which are also ignored in this repo—meaning they won’t run without proper configuration.

## Deliverables

### Deliverable 1 (March 4)
- Checkout the `Deliverable_1` branch to see the project state at Deliverable 1.

### Deliverable 2 (March 15)
- Checkout the `Deliverable_2` branch to see the project state at Deliverable 2.

### Deliverable 3 (March 24 - Final)
- Checkout the `Deliverable_3` branch to see the project state at Deliverable 3, or just view the main branch.
