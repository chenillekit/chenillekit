/**
 * list all files from directory /tmp
 */
def result = ""
dir = new File("/tmp")

dir.eachFileRecurse {
    result = result + "<br/>" + it.name
}

return result
