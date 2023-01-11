# Migrating Sunflower to Compose

Originally, Sunflower was meant to showcase best practices for several Jetpack libraries such as Jetpack Navigation, Room, ViewPager2, and more. Given this starting point, Sunflower now demonstrates how you can migrate a View-based app to Compose so that you can take a similar approach to migrate your app to Compose.

As of 2022, Sunflower demonstrates a partially migrated View-based app. The intent is to fully migrate Sunflower so that the UI layer is only in Compose.

This document captures the high-level strategy, as well as links to issues/pull requests for migration steps, taken to migrate the rest of the app to be Compose-only.

The general steps followed to migrate Sunflower to Compose are:

1. Planning the migration approach
2. Migrate existing screens to Compose one by one
3. Migrate Navigation Component to Compose and remove fragments

## #1 Planning the migration approach

Status: Done ✅

Before starting the migration process, it's best to think about the overall strategy to follow to incrementally migrate the entire app to Compose. The recommended [migration strategy](https://developer.android.com/jetpack/compose/interop/migration-strategy) is to start introducing Compose by using it for new features you build. In Sunflower's case, we won't be adding new features, instead, each screen needs to be migrated to Compose one by one followed by replacing Fragment-based navigation with Navigation Compose. This approach is also called the *bottom-up* approach to migration.

See [migration strategy](https://developer.android.com/jetpack/compose/interop/migration-strategy) to learn more.

## #2 Migrate existing screens one by one

Status: In Progress

The Sunflower app has 5 distinct screens. Each screen needs to be migrated to Compose before moving to step 3. You can see the linked issues/pull requests below to follow progress or learn more about how each screen was migrated. 

| Screen  | Status |
| ------- | -------|
| GalleryFragment | TODO [#816](https://github.com/android/sunflower/issues/816) |
| GardenFragment | TODO [#814](https://github.com/android/sunflower/issues/814) |
| HomeViewPagerFragment | TODO [#813](https://github.com/android/sunflower/issues/813) |
| PlantDetailFragment | Done [#638](https://github.com/android/sunflower/pull/638) |
   
## #3 Migrate Navigation Component to Compose

Status: TODO

The last step in the migration process is to replace Fragment-based navigation with Jetpack Navigation, to use [Navigation Compose](https://developer.android.com/jetpack/compose/navigation).