package org.northshore.cbri.dict

class LoadICD0Files {
    public static void main(args) {
        File morphConcepts = new File(this.class.getResource('/dict/Morphenglish.txt').toURI())
        morphConcepts.eachLine { String line ->
            String[] fields = line.split('\t')
            println line
            println "\tfields[0]: ${fields[0]}"
            println "\tfields[1]: ${fields[1]}"
            println "\tfields[2]: ${fields[2]}"
            println '--------------------------------------'
        }
    }
}
