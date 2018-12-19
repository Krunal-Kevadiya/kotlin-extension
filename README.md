
[![API](https://img.shields.io/badge/API-16%2B-red.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Build Status](https://travis-ci.org/wupdigital/android-maven-publish.svg?branch=master)](https://github.com/Krunal-Kevadiya/Kotlin-Extension)
[ ![Download](https://api.bintray.com/packages/kevadiyakrunalk/Kotlin-Extension/kotlin-extension/images/download.svg) ](https://bintray.com/kevadiyakrunalk/Kotlin-Extension/kotlin-extension/_latestVersion)
[![License](https://img.shields.io/badge/License-Apache%202.0-orange.svg)](https://opensource.org/licenses/Apache-2.0)

# **Small library that contains common extensions for Android**

* Provide the shortest way to do things.
* Reduce count of "Compat" and "Utils" classes.
* Remove boilerplate code.

# Learn Kotlin development with:
* Kotlin Clean Architecture (MVVM And Project Setting)
* Advance level setup Gradle like (version, source, signature, flavour, buildType, dependencies, repositories).
* LiveData.
* ViewModel.
* DataBinding.
* Room.
* Lifecycle Architecture Components.
* Retrofit 2 with dynamic url change without create retrofit object and CallbackWrapper to customise response.
* Dagger 2.
* RxJava 2.

###### Also create so many extensions to use generally like 
1. **ActivityExtensions** to allow full-screen, toggle statusBar, show/hide toolbar and show/hide keyboard.
2. **AlertExtensions** to allow alert, confirm and selector dialog.
3. **BitmapExtensions** to allow cut rounded corner, save bitmap, download, resize etc.
4. **ClipboardExtensions** to allow copy and get text in clipboard.
5. **ContactProviderExtensions** to get phone contact.
6. **ContextExtensions** to allow hide/show keyboard
7. **DateExtensions** to allow convert date format.
8. **DimenExtensions** to get and convert dime value in dip, px, sp.
9. **DrawableExtensions** search and get image from resource.
10. **FileExtensions** to get path, download, save and read file.
11. **FontExtensions** to apply custom font.
12. **JsonExtensions** to convert json to different type of data.
13. **KeyHashExtensions** to get Facebook hash string.
14. **LayoutExtensions** to inflate layout.
15. **LocationExtensions** to get current location and address base on provider.
16. **LogExtensions** to error, debug, shout, json, exception logical print in android console.
17. **ManagerExtensions** to get system services.
18. **NavigationExtensions** to use launch screen and manage fragment.
19. **NetworkExtensions** to use get current network(WIFI/MOBILE) and check internet availability.
20. **PermissionExtensions** to use runtime permission check in above Marshmallow.
21. **PhotoExtensions** to allow get image width, height, mime-type, orientation etc.
22. **PickMediaExtensions** to get image and video from gallery and camera.
23. **SharePreferenceExtensions** to use store app data in locally.
24. **TimeExtensions** to allow plus, minus time and scheduler set.
25. **ToastAndSnackBarExtensions** to user acknowledgement purpose.
26. **VersionExtensions** to get application version code and name.
27. **ValidationExtensions** to validate EditText and TextInputEditText field like(email, noNumber, nonEmpty, allUpperCase, allLowerCase, atLeastOneLowerCase etc).
28. **RecyclerView adapter** with support multiple view holder with data binding or without data binding, diffutils, loadmore listener with custom support and empty/error layout attach with custom support.
29. **[Fastlane integration](https://docs.fastlane.tools/getting-started/android/setup/)** with auto increment build number with product flavors option and upload in fabric. build number pattern like 1.0.0(0) [major.minor.patch(build)], Also Rename and copy apk file in Settings/apk dir, so the every time of releasing to auto backup apk file.
 
**You have just run fastlane command like below**
> fastlane devVariant versionChange:"build" isNote:false
 
> fastlane qaVariant versionChange:"build" isNote:false

> fastlane productionVariant versionChange:"build"

### How to use Recycler Adapter?
**How to user without data binding**
```kotlin
RecyclerAdapter.create()
        .register<User>(R.layout.recycler_item_user) { data, injector ->
            injector.text(R.id.name, data.name)
                    .image(R.id.avatar, data.avatarRes)
                    .text(R.id.phone, data.phone)
                    .textColor(R.id.phone, Color.RED)
                    .textSize(R.id.phone, 12)
       }
        .register<SectionHeader>(R.layout.recycler_item_setion_header) { data, injector ->
            injector.text(R.id.section_title, data.title)
                    .clicked(R.id.lny_main) {
                        context.shortToast("Section Header Clicked -> " + data.title)
                    }
        }
        .register<Image>(R.layout.recycler_item_image) { data, injector ->
            injector.with<ImageView>(R.id.img_cover, object : IViewInjector.Action<ImageView> {
                        override fun action(view: ImageView?) {
                            view?.setImageResource(data.res)
                        }
                    })
                    .longClicked(R.id.img_cover) {
                        context.shortToast("Image LongClicked -> " + data.res)
                        true
                    }
        }
        .register<Music>(R.layout.recycler_item_music) { data, injector ->
            injector.text(R.id.name, data.name)
                    .image(R.id.cover, data.coverRes)
                    .clicked(R.id.movie_item) {
                        context.shortToast("Music Clicked -> " + data.name)
                    }
        }
        .enableDiff()
        .enableLoadMore(object: RecyclerMoreLoader(context, object:     SimpleLoadMoreViewCreator(context){
            override fun createNoMoreHint(): CharSequence = getString(R.string.txt_no_more_data)
        }){
            override fun onLoadMore(handler: Handler?) {
                SystemClock.sleep(3_000L);
                if (random.nextInt(10) > 7) {
                    handler!!.error()
                } else {
                    handler!!.loadCompleted(data1)
                    loadTime++
                }
            }
            override fun hasMore(): Boolean = loadTime < 3
        })
        .enableEmptyError(object: RecyclerEmptyErrorLoader(context, object: SimpleEmptyErrorViewCreator(context) {
        }){
            override fun onLoadEmptyError(handler: Handler?) {
                SystemClock.sleep(3_000L);
                if (random.nextInt(10) > 7) {
                    handler!!.error()
                } else {
                    handler!!.loadCompleted(data)
                }
            }
            override fun hasEmptyError(): Int {
                return 0
            }
        })
        .attachTo(recyclerView)
```        
**With data binding**
```kotlin
RecyclerAdapter.create()
        .register<NewsBean, RecyclerItemNewsBinding>(R.layout.recycler_item_news) { data, injector, binding ->
        }
        .enableDiff()
        .attachTo(recyclerView) 
```

# Add Build.gradle file
```gradle
//Add Dependencies for app level build.gradle
repositories {
    jcenter()
}
dependencies {
  implementation 'com.kevadiyakrunalk:kotlinextensions:1.0.0'
}
```
or Maven:
```xml
<dependency>
    <groupId>com.kevadiyakrunalk</groupId>
    <artifactId>kotlinextensions</artifactId>
    <version>1.0.0</version>
    <type>pom</type>
</dependency>
```
