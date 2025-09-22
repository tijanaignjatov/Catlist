# Catalist – Cat Breeds Catalog  

**Catalist** is an Android mobile application focused on exploring and learning about **cat breeds**.  
The project was developed as part of the **Mobile Applications Development course (RAF)**.  



## Project Overview  

The app provides users with a **catalog of cat breeds**, offering detailed breed information, images, and search functionality.  
All data is fetched from **TheCatAPI**.  



## Core Features  

1. **Breeds List Screen**  
   - Displays a list of all available cat breeds.  
   - Each item shows:  
     - Breed name  
     - Alternative names  
     - Shortened description (up to 250 characters)  
     - Up to 5 temperament traits (displayed as chips)  
   - Clicking a breed opens its detailed screen.  

2. **Breeds Details Screen**  
   - Shows:  
     - At least one breed photo  
     - Breed name  
     - Full description  
     - Country of origin  
     - All temperament traits  
     - Life span  
     - Average weight/height  
     - At least 5 additional behavior/trait widgets (e.g., adaptability, intelligence, vocalisation).  
   - Indicates if the breed is rare.  
   - Includes a button to open the breed’s Wikipedia page.  

3. **Search Breeds**  
   - Search breeds by name.  
   - Search results use the same list design as the main list.  
   - Clicking a search result opens the breed’s detail screen.  
   - Search can be reset to show all breeds again.  



## Technical Requirements  

- **Language:** Kotlin  
- **Architecture:** MVI (Model-View-Intent)  
- **UI:** Jetpack Compose + Material Design 3  
- **Navigation:** Jetpack Navigation  
- **Async:** Kotlin Coroutines  
- **Networking:** Retrofit & OkHttp (or Ktorfit & Ktor Client)  
- **Serialization:** Kotlinx Serialization  
- **Dependency Injection:** Hilt (or Koin)  
- **State Handling:** Supports success, loading, and error states  
- **Orientation Support:** Works in both portrait & landscape without losing state  



## Visual Requirements  

- UI built with **Material Design 3** components.  
- Creative freedom in design.  
- Proper usage of navigation, layouts, and responsive design.  



## API  

The app uses **[TheCatAPI](https://thecatapi.com/)** to fetch breed data and images.  

### Important Endpoints  
- Get all breeds:  
  `GET https://api.thecatapi.com/v1/breeds`  

- Get breed details by ID:  
  `GET https://api.thecatapi.com/v1/breeds/{breed_id}`  

- Get breed images:  
  `GET https://api.thecatapi.com/v1/images/search?breed_ids={id}`  

- Search breeds by query:  
  `GET https://api.thecatapi.com/v1/breeds/search?q={query}`  

- Get specific image details:  
  `GET https://api.thecatapi.com/v1/images/{image_id}`  



## Project Structure  

- `app/` – Main application module  
- `ui/` – Compose UI screens (Breeds list, details, search)  
- `data/` – API models, repositories  
- `navigation/` – Jetpack Navigation setup  
- `di/` – Hilt dependency injection setup
