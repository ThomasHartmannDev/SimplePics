## SimplePics App

SimplePics is a simple picture-sharing application built with Android's Jetpack Compose and Firebase. Users can sign up, log in, post pictures, follow other users, like posts, and comment on posts.

### Features

- User Authentication (Sign up, Log in, Forgot Password)
- Create and view posts with images and descriptions
- Like and comment on posts
- Follow and unfollow users
- Profile management
- Feed with personalized and general posts

### Technologies Used

- Jetpack Compose for UI
- Firebase Authentication for user management
- Firebase Firestore for database
- Firebase Storage for storing images
- Dagger Hilt for dependency injection

### Architecture

The app follows MVVM (Model-View-ViewModel) architecture. 

- **Model:** Data classes and Firebase services.
- **View:** Composables for UI.
- **ViewModel:** Handles business logic and LiveData for the UI to observe.

### Project Structure

- `data`: Contains data classes and event handling classes.
- `hilt`: Provides Firebase dependencies using Dagger Hilt.
- `ui`: Contains all the UI-related code, including screens and components.
- `viewmodels`: Contains ViewModels for managing UI-related data.

### Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/simplepics.git
   cd simplepics
   ```

2. **Open the project in Android Studio.**

3. **Set up Firebase:**
   - Add your `google-services.json` file to the `app` directory.
   - Ensure Firebase Authentication, Firestore, and Storage are enabled in your Firebase project.

4. **Build and Run the project.**

### Screenshots

It will be add in the future!

### License

This project is licensed under the MIT License.

---

Feel free to contribute to this project by creating issues or submitting pull requests. 

Happy coding! 🎉
