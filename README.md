# FilterableListDialog
An Android list dialog with filtering the list items

![Current Version](https://img.shields.io/badge/currentVersion-0.0.1-green.svg)
![Minimum SDK](https://img.shields.io/badge/minSdkVersion-14-orange.svg)

## Usage

### Gradle Dependency

```gradle
repositories {
    maven {
        url 'https://dl.bintray.com/rohan2817/FilterableListDialog'
    }
}

dependencies {
    compile 'me.rohanpeshkar.filterablelistdialog:filterablelistdialog:0.0.1'
}
```

### Using Dialog

Using list dialog is pretty simple, it needs a list of items to be shown in dialog. For now it needs to be `ArrayList<String>`. 
Example Usage :


```java
FilterableListDialog.create(this, getListItems(),
             new FilterableListDialog.OnListItemSelectedListener() {
                 @Override
                 public void onItemSelected(String item) {
                      //Use selected item as needed
                    }
                 }).show();
```
`getListItems()` will return list of items to be shown in dialog.

Warning: Please remember to call `show()` method as `FilterableListDialog.create()` will only return the instance and does not show the dialog.
