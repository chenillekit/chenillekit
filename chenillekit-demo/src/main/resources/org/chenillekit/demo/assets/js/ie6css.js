function changeClasses(currentdoc)
{
    var inputElements = currentdoc.getElementsByTagName('input');
    for (var i = 0; i < inputElements.length; i++)
    {
        if (inputElements[i].type == "submit")
            inputElements[i].className = inputElements[i].className + " submitbutton";
        else if (inputElements[i].type == "checkbox")
            inputElements[i].className = inputElements[i].className + " checkbox";
        else if (inputElements[i].type == "radio")
            inputElements[i].className = inputElements[i].className + " radio";
        else if (inputElements[i].type == "text")
            inputElements[i].className = inputElements[i].className + " textbox";
    }
}
