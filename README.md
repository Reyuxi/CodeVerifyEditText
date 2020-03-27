# CodeVerifyEditText [ ![Download](https://api.bintray.com/packages/reyuxi/AndroidUILibrary/CodeVerifyEditText/images/download.svg?version=1.0.1) ](https://bintray.com/reyuxi/AndroidUILibrary/CodeVerifyEditText/1.0.1/link)

A editor for entering verification codes.

# Usage

1. Add the following code to the gradle file of your project module:

```Gradle
implementation 'cn.yuxirey.codeverifyedittext:codeverifyeditext:1.0.1'
```

2. Use it just like an EditText ;P

## Attributes

| Name                    | Descriptions                                                                                                            |   Default    |
| :---------------------- | :---------------------------------------------------------------------------------------------------------------------- | :----------: |
| cvedt_text              | Set the content text                                                                                                    |      -       |
| cvedt_textSize          | Set text size                                                                                                           |     14sp     |
| cvedt_textInactiveColor | Set the color of the text before the last character. (the last character is the character being typed. It is wired :P). |   #999999    |
| cvedt_textActiveColor   | The color of last character.                                                                                            |   #333333    |
| cvedt_textStyle         | Set the text style. This could be normal or bold.                                                                       |    normal    |
| cvedt_maxLength         | Set the allowed text length. It should be 4 or 6                                                                        |      4       |
| cvedt_lineHeight        | Set the underline height                                                                                                |     1dp      |
| cvedt_lineMarginTop     | Set the margin between code and underline                                                                               |     16dp     |
| cvedt_lineWidth         | Set the underline width                                                                                                 | TextWidth +8 |
| cvedt_lineInactiveColor | Like cvedt_textInActiveColor,set for the underline                                                                      | Same as text |
| cvedt_lineActiveColor   | Like cvedt_textInActiveColor,set for the underline                                                                      | Same as text |
| cvedt_onlyShownLine     | If set to true, only the underline with numbers will be displayed                                                       |    false     |

It can also be set in code.

## Callback

You can "listen" if the verification code has been entered by setting an `OnCompleteListener`. Just simply call the `setOnCompleteListener(OnCompleteListener listener)` method and handle your own code in callback method `void onComplete(String code)`.
