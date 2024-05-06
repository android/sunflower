# Make your Android app to Multiplatform app
https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-integrate-in-existing-app.html

## Migration path
- [x] Migrate XML views to Compose
- [ ] Migrate non-KMP libraries to KMP
    - [ ] Migrate Gson to kotlinx.serialization
    - [ ] Migrate Retrofit to Ktor
    - [ ] Migrate Glide to Coil
    - [ ] Migrate Room to SQLDelight
    - [ ] Migrate Dagger to Koin
- [ ] Migrate `res` files with Multiplatform Resources
- [ ] Rename `:app` to `:androidApp`
- [ ] Create `:shared` module
- [ ] Move sharable code to `commonMain` in `:shared`
- [ ] Create `:desktopApp` module
- [ ] Create `:iosApp` module
