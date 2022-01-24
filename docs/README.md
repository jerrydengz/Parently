Parently
- an android parenting app to help parents manage their children

Cautions:
=========
* Make sure animations are enabled on your testing device, coin flip will not properly work w/o them
* Make sure you properly sync your gradle for our custom imports. If either GSON or konfetti error, refresh.
  We've found just commenting out a "implementation ...", and then un-commenting it to trigger a sync works

Style Guide Reference:
======================
- based off Twitter's style guide + modifications based on the Team's preference
- https://github.com/twitter-archive/commons/blob/master/src/java/com/twitter/common/styleguide.md

Style Guide Modification Off Twitter Style Guide:
=========================
1. Okay to use tabs for spacing. Android studio converts tabs to spaces, so it's interchangeable.
2. For chained method calls, should separate them on new lines if there are 3 or more of them

    :::java
    // 3 or more chained method calls
    object.method()
          .method()
          .method();

    // 1 or 2 chained method calls
    object.method().method();

3. Ok to not end `try/catch` blocks with `finally`
4. Add `this.` for class fields to make distinction between local variables and class fields
5. Use camelCase for R.id tag's and snake_case for R.string tag's

Resource Ref:
=============
- Coin sides: https://en.wikipedia.org/wiki/Quarter_(Canadian_coin)
- Coin Flip sound: https://www.youtube.com/watch?v=Hy7DORHmiKY
- Victory sound: https://www.youtube.com/watch?v=lcJH8JtgZoE
- Defeat sound: https://www.youtube.com/watch?v=8Xt1kUK71lc
- Timeout activity timer bg: https://wallpaperaccess.com/minimal-phone
- App Icon: https://www.pngfind.com/download/hwbwiwi_child-icon-png-children-protection-png-transparent-png/
- Main menu background: https://www.pinterest.ca/pin/318066792439069784/
- General background images courtesy of https://www.manku.dev/
- Exhale/Inhale Sounds: https://www.youtube.com/watch?v=n_-9mrBMLA8
