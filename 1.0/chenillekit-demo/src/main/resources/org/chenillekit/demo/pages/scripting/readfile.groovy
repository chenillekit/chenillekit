/**
 * list all files from directory /tmp
 */

context = bsf.lookupBean("web_context");
text = "<h4>some senseless text readed from file dependencies.txt</h4><br/>"

context.getRealFile("/WEB-INF/dependencies.txt").eachLine {
    text = "$text$it<br/>"
}

return text