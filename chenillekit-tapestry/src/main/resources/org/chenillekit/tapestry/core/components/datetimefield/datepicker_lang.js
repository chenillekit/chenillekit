/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

Control.DatePicker.Locale['pt'] = {
	dateTimeFormat: 'dd/MM/yyyy HH:mm',
	dateFormat: 'dd/MM/yyyy',
	firstWeekDay: 0,
	weekend: [0,6],
	timeFormat: 'HH:mm',
	language: 'pt'
};

Control.DatePicker.Locale['lt_LT'] = {
	dateTimeFormat: 'yyyy-MM-dd HH:mm',
	dateFormat: 'yyyy-MM-dd',
	firstWeekDay: 1,
	weekend: [0,6],
	language: 'lt'
};

Control.DatePicker.Locale['ru'] = {
	dateTimeFormat: 'dd-MM-yyyy HH:mm',
	dateFormat: 'dd-MM-yyyy',
	firstWeekDay: 1,
	weekend: [0,6],
	timeFormat: 'HH:mm',
	language: 'ru'
};

with (Control.DatePicker) Locale['pt'] = i18n.createLocale('pt', 'pt');
with (Control.DatePicker) Locale['fr'] = i18n.createLocale('eu', 'fr');
with (Control.DatePicker) Locale['lt'] = i18n.createLocale('lt', 'lt');
with (Control.DatePicker) Locale['ru'] = i18n.createLocale('ru', 'ru');

Control.DatePicker.Language['pt'] = {
	months: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho','Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
	days: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sab'],
	strings: {
		'Now': 'Agora',
		'Today': 'Hoje',
		'Time': 'Tempo',
		'Exact minutes': 'Minutos Exatos',
		'Select Date and Time': 'Selecionar Data e Hora',
		'Open calendar': 'Abrir calendário'
	}
};

Control.DatePicker.Language['lt'] = {
	months: ['Sausis', 'Vasaris', 'Kovas', 'Balandis', 'Gegužė', 'Birželis',
		'Liepa', 'Rugpjūtis', 'Rugsėjis', 'Spalis', 'Lapkritis', 'Gruodis'],
	days: ['Sek', 'Pir', 'Ant', 'Tre', 'Ket', 'Pen', 'Šeš'],
	strings: {
		'Now': 'Dabar',
		'Today': 'Šiandien',
		'Time': 'Laikas',
		'Exact minutes': 'Tikslios minutės',
		'Select Date and Time': 'Pasirink datą ir laiką',
		'Open calendar': 'Atverti kalendorių'
	}
};

Control.DatePicker.Language['ru'] = {
	months: ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'],
	days: ['Вс', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб'],
	strings: {
		'Now': 'Сейчас',
		'Today': 'Сегодня',
		'Time': 'Время',
		'Exact minutes': 'Точно минут',
		'Select Date and Time': 'Выберите Дату и Время',
		'Open calendar': 'Открыть календарь'
	}
};

Control.DatePicker.Language['fr'] = {
	months: ['Janvier', 'Février', 'Märs', 'Avril', 'Mai', 'Juin', 'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre'],
	days: ['Di', 'Lu', 'Ma', 'Me', 'Je', 'Ve', 'Sa'],
	strings: {
		'Now': 'Maintenant',
		'Today': 'Aujourd\'hui',
		'Time': 'Heure',
		'Exact minutes': 'Minutes exactes',
		'Select Date and Time': 'Sélectionner date et heure',
		'Open calendar': 'Ouvrir le calendrier'
	}
};
