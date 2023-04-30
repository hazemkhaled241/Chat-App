package com.hazem.chat.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.*
import androidx.navigation.NavController
import com.google.i18n.phonenumbers.PhoneNumberUtil

import com.hazem.chat.R
import com.hazem.chat.data.local.Country


@SuppressLint("InflateParams")
fun Context.createAlertDialog(activity:Activity): Dialog {
   val dialogTransparent = Dialog(this, android.R.style.Theme_Black)
    val view: View = LayoutInflater.from(activity).inflate(
        R.layout.progress_dialog, null
    )
    dialogTransparent.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialogTransparent.window?.setBackgroundDrawableResource(
        R.color.transparent
    )

    dialogTransparent.setCancelable(false)
    dialogTransparent.setContentView(view)
    return dialogTransparent
}
fun countries():ArrayList<Country>{
    return arrayListOf(
        Country(0,"AF", "Afghanistan", "+93", "AFN"),
        Country(0,"AX", "Aland Islands", "+358", "EUR"),
        Country(0,"AL", "Albania", "+355", "ALL"),
        Country(0,"DZ", "Algeria", "+213", "DZD"),
        Country(0,"AS", "American Samoa", "+1", "USD"),
        Country(0,"AD", "Andorra", "+376", "EUR"),
        Country(0,"AO", "Angola", "+244", "AOA"),
        Country(0,"AI", "Anguilla", "+1", "XCD"),
        Country(0,"AQ", "Antarctica", "+672", "USD"),
        Country(0,"AG", "Antigua and Barbuda", "+1", "XCD"),
        Country(0,"AR", "Argentina", "+54", "ARS"),
        Country(0,"AM", "Armenia", "+374", "AMD"),
        Country(0,"AW", "Aruba", "+297", "AWG"),
        Country(0,"AU", "Australia", "+61", "AUD"),
        Country(0,"AT", "Austria", "+43", "EUR"),
        Country(0,"AZ", "Azerbaijan", "+994", "AZN"),
        Country(0,"BS", "Bahamas", "+1", "BSD"),
        Country(0,"BH", "Bahrain", "+973", "BHD"),
        Country(0,"BD", "Bangladesh", "+880", "BDT"),
        Country(0,"BB", "Barbados", "+1", "BBD"),
        Country(0,"BY", "Belarus", "+375", "BYR"),
        Country(0,"BE", "Belgium", "+32", "EUR"),
        Country(0,"BZ", "Belize", "+501", "BZD"),
        Country(0,"BJ", "Benin", "+229", "XOF"),
        Country(0,"BM", "Bermuda", "+1", "BMD"),
        Country(0,"BT", "Bhutan", "+975", "BTN"),
        Country(0,"BO", "Bolivia, Plurinational State of", "+591", "BOB"),
        Country(0,"BA", "Bosnia and Herzegovina", "+387", "BAM"),
        Country(0,"BQ", "Bonaire", "+599", "USD"),
        Country(0,"BW", "Botswana", "+267", "BWP"),
        Country(0,"BV", "Bouvet Island", "+47", "NOK"),
        Country(0,"BR", "Brazil", "+55", "BRL"),
        Country(0,"IO", "British Indian Ocean Territory", "+246", "USD"),
        Country(0,"BN", "Brunei Darussalam", "+673", "BND"),
        Country(0,"BG", "Bulgaria", "+359", "BGN"),
        Country(0,"BF", "Burkina Faso", "+226", "XOF"),
        Country(0,"BI", "Burundi", "+257", "BIF"),
        Country(0,"KH", "Cambodia", "+855", "KHR"),
        Country(0,"CM", "Cameroon", "+237", "XAF"),
        Country(0,"CA", "Canada", "+1", "CAD"),
        Country(0,"CV", "Cape Verde", "+238", "CVE"),
        Country(0,"KY", "Cayman Islands", "+345", "KYD"),
        Country(0,"CF", "Central African Republic", "+236", "XAF"),
        Country(0,"TD", "Chad", "+235", "XAF"),
        Country(0,"CL", "Chile", "+56", "CLP"),
        Country(0,"CN", "China", "+86", "CNY"),
        Country(0,"CX", "Christmas Island", "+61", "AUD"),
        Country(0,"CC", "Cocos (Keeling) Islands", "+61", "AUD"),
        Country(0,"CO", "Colombia", "+57", "COP"),
        Country(0,"KM", "Comoros", "+269", "KMF"),
        Country(0,"CD", "Congo, The Democratic Republic of the", "+243", "CDF"),
        Country(0,"CG", "Congo", "+242", "XAF"),
        Country(0,"CK", "Cook Islands", "+682", "NZD"),
        Country(0,"CR", "Costa Rica", "+506", "CRC"),
        Country(0,"HR", "Croatia", "+385", "HRK"),
        Country(0,"CU", "Cuba", "+53", "CUP"),
        Country(0,"CW", "Curacao", "+599", "ANG"),
        Country(0,"CY", "Cyprus", "+357", "EUR"),
        Country(0,"CZ", "Czech Republic", "+420", "CZK"),
        Country(0,"DK", "Denmark", "+45", "DKK"),
        Country(0,"DJ", "Djibouti", "+253", "DJF"),
        Country(0,"DM", "Dominica", "+1", "XCD"),
        Country(0,"DO", "Dominican Republic", "+1", "DOP"),
        Country(0,"TL", "East Timor", "+670", "USD"),
        Country(0,"EC", "Ecuador", "+593", "USD"),
        Country(0,"EG", "Egypt", "+20", "EGP"),
        Country(0,"SV", "El Salvador", "+503", "SVC"),
        Country(0,"GQ", "Equatorial Guinea", "+240", "XAF"),
        Country(0,"ER", "Eritrea", "+291", "ERN"),
        Country(0,"EE", "Estonia", "+372", "EUR"),
        Country(0,"ET", "Ethiopia", "+251", "ETB"),
        Country(0,"FK", "Falkland Islands (Malvinas)", "+500", "FKP"),
        Country(0,"FO", "Faroe Islands", "+298", "DKK"),
        Country(0,"FJ", "Fiji", "+679", "FJD"),
        Country(0,"FI", "Finland", "+358", "EUR"),
        Country(0,"FR", "France", "+33", "EUR"),
        Country(0,"GF", "French Guiana", "+594", "EUR"),
        Country(0,"TF", "French Southern Territories", "+262", "EUR"),
        Country(0,"PF", "French Polynesia", "+689", "XPF"),
        Country(0,"GA", "Gabon", "+241", "XAF"),
        Country(0,"GM", "Gambia", "+220", "GMD"),
        Country(0,"GE", "Georgia", "+995", "GEL"),
        Country(0,"DE", "Germany", "+49", "EUR"),
        Country(0,"GH", "Ghana", "+233", "GHS"),
        Country(0,"GI", "Gibraltar", "+350", "GIP"),
        Country(0,"GR", "Greece", "+30", "EUR"),
        Country(0,"GL", "Greenland", "+299", "DKK"),
        Country(0,"GD", "Grenada", "+1", "XCD"),
        Country(0,"GP", "Guadeloupe", "+590", "EUR"),
        Country(0,"GU", "Guam", "+1", "USD"),
        Country(0,"GT", "Guatemala", "+502", "GTQ"),
        Country(0,"GG", "Guernsey", "+44", "GGP"),
        Country(0,"GN", "Guinea", "+224", "GNF"),
        Country(0,"GW", "Guinea-Bissau", "+245", "XOF"),
        Country(0,"GY", "Guyana", "+595", "GYD"),
        Country(0,"HT", "Haiti", "+509", "HTG"),
        Country(0,"HM", "Heard Island and McDonald Islands", "+000", "AUD"),
        Country(0,"VA", "Holy See (Vatican City State)", "+379", "EUR"),
        Country(0,"HN", "Honduras", "+504", "HNL"),
        Country(0,"HK", "Hong Kong", "+852", "HKD"),
        Country(0,"HU", "Hungary", "+36", "HUF"),
        Country(0,"IS", "Iceland", "+354", "ISK"),
        Country(0,"IN", "India", "+91", "INR"),
        Country(0,"ID", "Indonesia", "+62", "IDR"),
        Country(0,"IR", "Iran, Islamic Republic of", "+98", "IRR"),
        Country(0,"IQ", "Iraq", "+964", "IQD"),
        Country(0,"IE", "Ireland", "+353", "EUR"),
        Country(0,"IM", "Isle of Man", "+44", "GBP"),
        Country(0,"IL", "Israel", "+972", "ILS"),
        Country(0,"IT", "Italy", "+39", "EUR"),
        Country(0,"CI", "Ivory Coast", "+225", "XOF"),
        Country(0,"JM", "Jamaica", "+1", "JMD"),
        Country(0,"JP", "Japan", "+81", "JPY"),
        Country(0,"JE", "Jersey", "+44", "JEP"),
        Country(0,"JO", "Jordan", "+962", "JOD"),
        Country(0,"KZ", "Kazakhstan", "+7", "KZT"),
        Country(0,"KE", "Kenya", "+254", "KES"),
        Country(0,"KI", "Kiribati", "+686", "AUD"),
        Country(0,"XK", "Kosovo", "+383", "EUR"),
        Country(0,"KW", "Kuwait", "+965", "KWD"),
        Country(0,"KG", "Kyrgyzstan", "+996", "KGS"),
        Country(0,"LA", "Lao People's Democratic Republic", "+856", "LAK"),
        Country(0,"LV", "Latvia", "+371", "LVL"),
        Country(0,"LB", "Lebanon", "+961", "LBP"),
        Country(0,"LS", "Lesotho", "+266", "LSL"),
        Country(0,"LR", "Liberia", "+231", "LRD"),
        Country(0,"LY", "Libyan Arab Jamahiriya", "+218", "LYD"),
        Country(0,"LI", "Liechtenstein", "+423", "CHF"),
        Country(0,"LT", "Lithuania", "+370", "LTL"),
        Country(0,"LU", "Luxembourg", "+352", "EUR"),
        Country(0,"MO", "Macao", "+853", "MOP"),
        Country(
            0,"MK", "Macedonia, The Former Yugoslav Republic of", "+389",
            "MKD"
        ),
        Country(0,"MG", "Madagascar", "+261", "MGA"),
        Country(0,"MW", "Malawi", "+265", "MWK"),
        Country(0,"MY", "Malaysia", "+60", "MYR"),
        Country(0,"MV", "Maldives", "+960", "MVR"),
        Country(0,"ML", "Mali", "+223", "XOF"),
        Country(0,"MT", "Malta", "+356", "EUR"),
        Country(0,"MH", "Marshall Islands", "+692", "USD"),
        Country(0,"MQ", "Martinique", "+596", "EUR"),
        Country(0,"MR", "Mauritania", "+222", "MRO"),
        Country(0,"MU", "Mauritius", "+230", "MUR"),
        Country(0,"YT", "Mayotte", "+262", "EUR"),
        Country(0,"MX", "Mexico", "+52", "MXN"),
        Country(0,"FM", "Micronesia, Federated States of", "+691", "USD"),
        Country(0,"MD", "Moldova, Republic of", "+373", "MDL"),
        Country(0,"MC", "Monaco", "+377", "EUR"),
        Country(0,"MN", "Mongolia", "+976", "MNT"),
        Country(0,"ME", "Montenegro", "+382", "EUR"),
        Country(0,"MS", "Montserrat", "+1", "XCD"),
        Country(0,"MA", "Morocco", "+212", "MAD"),
        Country(0,"MZ", "Mozambique", "+258", "MZN"),
        Country(0,"MM", "Myanmar", "+95", "MMK"),
        Country(0,"NA", "Namibia", "+264", "NAD"),
        Country(0,"NR", "Nauru", "+674", "AUD"),
        Country(0,"NP", "Nepal", "+977", "NPR"),
        Country(0,"NL", "Netherlands", "+31", "EUR"),
        Country(0,"NC", "New Caledonia", "+687", "XPF"),
        Country(0,"NZ", "New Zealand", "+64", "NZD"),
        Country(0,"NI", "Nicaragua", "+505", "NIO"),
        Country(0,"NE", "Niger", "+227", "XOF"),
        Country(0,"NG", "Nigeria", "+234", "NGN"),
        Country(0,"NU", "Niue", "+683", "NZD"),
        Country(0,"NF", "Norfolk Island", "+672", "AUD"),
        Country(0,"MP", "Northern Mariana Islands", "+1", "USD"),
        Country(0,"KP", "North Korea", "+850", "KPW"),
        Country(0,"NO", "Norway", "+47", "NOK"),
        Country(0,"OM", "Oman", "+968", "OMR"),
        Country(0,"PK", "Pakistan", "+92", "PKR"),
        Country(0,"PW", "Palau", "+680", "USD"),
        Country(0,"PS", "Palestinian Territory, Occupied", "+970", "ILS"),
        Country(0,"PA", "Panama", "+507", "PAB"),
        Country(0,"PG", "Papua New Guinea", "+675", "PGK"),
        Country(0,"PY", "Paraguay", "+595", "PYG"),
        Country(0,"PE", "Peru", "+51", "PEN"),
        Country(0,"PH", "Philippines", "+63", "PHP"),
        Country(0,"PN", "Pitcairn", "+872", "NZD"),
        Country(0,"PL", "Poland", "+48", "PLN"),
        Country(0,"PT", "Portugal", "+351", "EUR"),
        Country(0,"PR", "Puerto Rico", "+1", "USD"),
        Country(0,"QA", "Qatar", "+974", "QAR"),
        Country(0,"RO", "Romania", "+40", "RON"),
        Country(0,"RU", "Russia", "+7", "RUB"),
        Country(0,"RW", "Rwanda", "+250", "RWF"),
        Country(0,"RE", "Reunion", "+262", "EUR"),
        Country(0,"BL", "Saint Barthelemy", "+590", "EUR"),
        Country(
            0,"SH", "Saint Helena, Ascension and Tristan Da Cunha", "+290",
            "SHP"
        ),
        Country(0,"KN", "Saint Kitts and Nevis", "+1", "XCD"),
        Country(0,"LC", "Saint Lucia", "+1", "XCD"),
        Country(0,"MF", "Saint Martin", "+590", "EUR"),
        Country(0,"PM", "Saint Pierre and Miquelon", "+508", "EUR"),
        Country(0,"VC", "Saint Vincent and the Grenadines", "+1", "XCD"),
        Country(0,"WS", "Samoa", "+685", "WST"),
        Country(0,"SM", "San Marino", "+378", "EUR"),
        Country(0,"ST", "Sao Tome and Principe", "+239", "STD"),
        Country(0,"SA", "Saudi Arabia", "+966", "SAR"),
        Country(0,"SN", "Senegal", "+221", "XOF"),
        Country(0,"RS", "Serbia", "+381", "RSD"),
        Country(0,"SC", "Seychelles", "+248", "SCR"),
        Country(0,"SL", "Sierra Leone", "+232", "SLL"),
        Country(0,"SG", "Singapore", "+65", "SGD"),
        Country(0,"SX", "Sint Maarten", "+1", "ANG"),
        Country(0,"SK", "Slovakia", "+421", "EUR"),
        Country(0,"SI", "Slovenia", "+386", "EUR"),
        Country(0,"SB", "Solomon Islands", "+677", "SBD"),
        Country(0,"SO", "Somalia", "+252", "SOS"),
        Country(0,"ZA", "South Africa", "+27", "ZAR"),
        Country(0,"SS", "South Sudan", "+211", "SSP"),
        Country(
            0,"GS", "South Georgia and the South Sandwich Islands", "+500",
            "GBP"
        ),
        Country(0,"KR", "South Korea", "+82", "KRW"),
        Country(0,"ES", "Spain", "+34", "EUR"),
        Country(0,"LK", "Sri Lanka", "+94", "LKR"),
        Country(0,"SD", "Sudan", "+249", "SDG"),
        Country(0,"SR", "Suriname", "+597", "SRD"),
        Country(0,"SJ", "Svalbard and Jan Mayen", "+47", "NOK"),
        Country(0,"SZ", "Swaziland", "+268", "SZL"),
        Country(0,"SE", "Sweden", "+46", "SEK"),
        Country(0,"CH", "Switzerland", "+41", "CHF"),
        Country(0,"SY", "Syrian Arab Republic", "+963", "SYP"),
        Country(0,"TW", "Taiwan", "+886", "TWD"),
        Country(0,"TJ", "Tajikistan", "+992", "TJS"),
        Country(0,"TZ", "Tanzania, United Republic of", "+255", "TZS"),
        Country(0,"TH", "Thailand", "+66", "THB"),
        Country(0,"TG", "Togo", "+228", "XOF"),
        Country(0,"TK", "Tokelau", "+690", "NZD"),
        Country(0,"TO", "Tonga", "+676", "TOP"),
        Country(0,"TT", "Trinidad and Tobago", "+1", "TTD"),
        Country(0,"TN", "Tunisia", "+216", "TND"),
        Country(0,"TR", "Turkey", "+90", "TRY"),
        Country(0,"TM", "Turkmenistan", "+993", "TMT"),
        Country(0,"TC", "Turks and Caicos Islands", "+1", "USD"),
        Country(0,"TV", "Tuvalu", "+688", "AUD"),
        Country(0,"UM", "U.S. Minor Outlying Islands", "+1", "USD"),
        Country(0,"UG", "Uganda", "+256", "UGX"),
        Country(0,"UA", "Ukraine", "+380", "UAH"),
        Country(0,"AE", "United Arab Emirates", "+971", "AED"),
        Country(0,"GB", "United Kingdom", "+44", "GBP"),
        Country(0,"US", "United States", "+1", "USD"),
        Country(0,"UY", "Uruguay", "+598", "UYU"),
        Country(0,"UZ", "Uzbekistan", "+998", "UZS"),
        Country(0,"VU", "Vanuatu", "+678", "VUV"),
        Country(0,"VE", "Venezuela, Bolivarian Republic of", "+58", "VEF"),
        Country(0,"VN", "Vietnam", "+84", "VND"),
        Country(0,"VG", "Virgin Islands, British", "+1", "USD"),
        Country(0,"VI", "Virgin Islands, U.S.", "+1", "USD"),
        Country(0,"WF", "Wallis and Futuna", "+681", "XPF"),
        Country(0,"EH", "Western Sahara", "+212", "MAD"),
        Country(0,"YE", "Yemen", "+967", "YER"),
        Country(0,"ZM", "Zambia", "+260", "ZMW"),
        Country(0,"ZW", "Zimbabwe", "+263", "USD")
    )

}

fun isValidNumber(phoneNumber:String,  code: String): Resource<String, String> {
   val phoneNumberUtil: PhoneNumberUtil=PhoneNumberUtil.getInstance()
    try {
    return if (code.isEmpty()) {
        Resource.Error("Please select code !")
    }else if (phoneNumber.isEmpty()) {
        Resource.Error("number can't be empty !")
    } else if (  !phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(phoneNumber, code)
        )) {
        Resource.Error("please enter a valid number !")
    }  else {
        Resource.Success("valid Number")
    }
    }

    catch (e: Exception) {
        e.printStackTrace()
    }
    return  Resource.Error("Something wrong happened !")
}
fun NavController.isValidDestination(destination: Int): Boolean {
    return destination == this.currentDestination!!.id
}
