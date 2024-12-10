# NYC Open Jobs App

The **NYC Open Jobs App** is an Android application built using Jetpack Compose, Material 3, and modern Android architecture principles. It provides a user-friendly interface for exploring job listings from New York City's open job data. Users can browse job listings, view detailed descriptions, and save their favorite jobs for easy access.

## Features

### 1. Browse Jobs
- Displays a list of job postings fetched from NYC's open job data.
- Includes job information like agency, title, salary range, and career level.

### 2. Search Functionality
- Users can filter job postings dynamically using a search bar.

### 3. Favorite Jobs
- Users can save jobs by clicking the heart icon on the job detail screen.
- Saved jobs appear in the "Favorites" tab for quick access.
- Favorites are persistent within the app's runtime.

### 4. Detailed Job Descriptions
- Clicking on a job posting or a favorite displays detailed information about the job, including:
  - Job description
  - Minimum qualifications
  - Salary range
  - Agency location

### 5. Material Design
- Implements Material 3 design principles for a modern and seamless user experience.

---

## Tech Stack

### Frontend
- **Jetpack Compose**: For declarative UI design.
- **Material 3**: Provides modern UI components.
- **Navigation Component**: For navigation between screens.

### Backend
- **Retrofit**: For fetching job data from NYC Open Data API.
- **Kotlin Coroutines**: For asynchronous data handling.

### Storage
- **Shared Preferences**: Used for saving temporary data like scroll position.

---

https://github.com/user-attachments/assets/bc8c976a-e60b-40d4-9236-352d624a96ba
