import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart' show rootBundle;
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/locale/SupportedLocales.dart';

class Translations {
  Translations(Locale locale) {
    this.locale = locale;
  }

  Locale? locale;
  static Map<dynamic, dynamic> _localizedValues = {};

  static String textOf(BuildContext context, String key) {
    Locale myLocale = Localizations.localeOf(context);

    Logs.info(
        "textOf is called...${myLocale.countryCode} ${myLocale.languageCode}");
    var text = _of(context)?.text(key);
    return text == null ? "" : text;
  }

  static Translations? _of(BuildContext context) {
    return Localizations.of<Translations>(context, Translations);
  }

  String text(String key) {
    return _localizedValues[key] ?? '** $key not found';
  }

  static Future<Translations> load(Locale locale) async {
    Logs.info("load is called...");
    Translations translations = new Translations(locale);
    String jsonContent = await rootBundle
        .loadString("assets/locale/i18n_${locale.languageCode}.json");
    _localizedValues = json.decode(jsonContent);
    return translations;
  }

  get currentLanguage => locale?.languageCode;
}

class TranslationsDelegate extends LocalizationsDelegate<Translations> {
  const TranslationsDelegate();

  @override
  bool isSupported(Locale locale) {
    List<Locale> locales = SupportedLocales.locales;
    for (Locale locale in locales) {
      if (locale.languageCode == locale.languageCode) {
        return true;
      }
    }
    return false;
  }

  @override
  Future<Translations> load(Locale locale) => Translations.load(locale);

  @override
  bool shouldReload(TranslationsDelegate old) => false;

  static TranslationsDelegate delegate = const TranslationsDelegate();
}
