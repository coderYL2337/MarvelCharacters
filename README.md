<!-- (This is a comment) INSTRUCTIONS: Go through this page and fill out any **bolded** entries with their correct values.-->

# AND101 Project 5 - Choose Your Own API

Submitted by: **Yan Lu**

Time spent: **20** hours spent in total

## Summary

**MarvelCharacters** is an android app that *that displays a random character from the Marvel API**
**Character Image, Character ID, comics containing the character, description of the character show on screen when user click"Get the next Character"button.**
**Relavant textViews and ImageView update when user search by character name."Search result is not found" toast message shows if no search result is found.**

If I had to describe this project in three (3) emojis, they would be: **😵‍💫😅📱**

## Application Features

<!-- (This is a comment) Please be sure to change the [ ] to [x] for any features you completed.  If a feature is not checked [x], you might miss the points for that item! -->

The following REQUIRED features are completed:

- [x] Make an API call to an API of your choice using AsyncHTTPClient
- [x] Display at least three (3) pieces of data for each API entry retrieved
- [x] A working Button requests a new API entry and updates the data displayed

The following STRETCH features are implemented:

- [x] Add a query to the API request
  - The query I added is ** "https://gateway.marvel.com/v1/public/characters?nameStartsWith=$encodedQuery&limit=1&ts=$timestamp&apikey=0bff0d5f2a8aef9c2fa3c961be718c51&hash=715dd55a201345eff232ce852580d415"**
- [x] Build a UI to allow users to add that query

The following EXTRA features are implemented:

- [x] List anything else that you added to improve the app!
    -[x]Toast message added when no search result is found
    -[x] Two more TextViews on top of the one imageview and two texviews.
    -[x]Eclipse feature added to comics name to save space. A maximum of two lines first show in the comics name TextView.
        Users can view more details by clicking ...
        Users can return to the maximum two-line textview of the comics name by clicking the text of comics names again.

## API Choice

My chosen API for this project is **Marvel API**.

## Video Demo

Here's a video / GIF that demos all of the app's implemented features:

<blockquote class="imgur-embed-pub" lang="en" data-id="a/cNRFx0V"  ><a href="//imgur.com/a/cNRFx0V">demoMarvelCharacter</a></blockquote><script async src="//s.imgur.com/min/embed.js" charset="utf-8"></script>

GIF created with **Kap**

<!-- Recommended tools:
- [Kap](https://getkap.co/) for macOS
- [ScreenToGif](https://www.screentogif.com/) for Windows
- [peek](https://github.com/phw/peek) for Linux. -->

## Notes

Here's a place for any other notes on the app, it's creation process, or what you learned this unit!

## License

Copyright **2023** **Yan Lu**

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
