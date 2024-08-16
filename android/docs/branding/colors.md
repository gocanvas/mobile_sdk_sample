# Colors

The color system that can be used to create a color scheme that reflects your brand or style.


Color Attribute                 | Theme Color Role | Default |Affected Ui Components | 
------------------------------- | -----------------| --------------| ----------------------| 
gc_sdk_color_primary            | colorPrimary       | ![#039de7](https://placehold.co/15x15/039de7/039de7.png) #039de7   | toolbar, dialog buttons, primary button, tint input fields | 
gc_sdk_color_primary_dark       | colorPrimaryDark   | ![#0077b3](https://placehold.co/15x15/0077b3/0077b3.png) #0077b3 | status bar | 
gc_sdk_color_accent             | colorAccent | ![#5fa3d0](https://placehold.co/15x15/5fa3d0/5fa3d0.png) #5fa3d0 | date & time pickers top area background, input fields selected text | 
gc_sdk_color_secondary  | colorControlActivated, colorSecondary |![#00BFA5](https://placehold.co/15x15/00BFA5/00BFA5.png) #00BFA5| progress bar, date & time pickers selected value, checkbox, input fields with captured values | 

By overriding these color attributes, you can easily change the styles of all the mentioned components used by the sdk.


**Applying your own colors:**

Override the color attributes in your `colors.xml` file

```xml
<resources>
  <color name="gc_sdk_color_primary">...</color>
  <color name="gc_sdk_color_accent">...</color>
  <color name="gc_sdk_color_primary_dark">...</color>
  <color name="gc_sdk_color_secondary">...</color>
</resources>
```

