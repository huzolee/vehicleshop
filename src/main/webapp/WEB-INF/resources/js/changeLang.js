function changeLang() {
    var langParamName = "lang";
    var selectedLang = getSelectedLang();
    updateParam(langParamName, selectedLang);
}

function getSelectedLang() {
    var langList = document.getElementById("langList");
    return  langList.options[langList.selectedIndex].value;
}

function updateParam(key, value) {
    var queryString = new String(location.search);

    if (queryString.length > 0) {
        if (queryString.indexOf(key) > -1) {
            var regExpPattern = key + "=([a-zA-Z]+)?";
            var regExp = new RegExp(regExpPattern);

            queryString = queryString.replace(regExp, key + "=" + value);
        } else {
            queryString += "\u0026" + key + "=" + value;
        }
    } else {
        queryString += "?" + key + "=" + value;
    }

    location.search = queryString;
}