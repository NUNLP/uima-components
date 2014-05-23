package org.northshore.cbri

import groovy.sql.Sql

class UmlsDictionaryFileCreator {
    static Sql sql = Sql.newInstance( 'jdbc:mysql://localhost/umls', 'root', '34-Olga', 'com.mysql.jdbc.Driver' )

    static void generateDictFile(File dictFile, Set<String> cuis) {
        BufferedWriter writer = dictFile.newWriter()
        String sql_str = 'select mrc.CUI, mrs.TUI, mrc.CODE, mrc.STR from mrconso as mrc inner join umls.MRSTY as mrs on mrs.CUI = mrc.CUI where mrs.CUI in ('
        cuis.each { sql_str += "'$it', " }
        sql_str += "'')"
        sql.eachRow(sql_str) { row ->
            String mentionType;
            switch (row.tui) {
                case 'T191': mentionType = 'DiseaseDisorderMention'; break;
                case 'T023': mentionType = 'AnatomicalSiteMention'; break;
            }
            def builder = new groovy.json.JsonBuilder()
            def root = builder {
                cui "${row.cui}"
                tui "${row.tui}"
                code "${row.code}"
                phrase "${row.str}"
                type mentionType
            }
            writer << builder.toString()
            writer << '\n'
            writer.flush()
        }
        writer.close()
    }

    static public void main(args) {
        File dictFile = new File('src/test/resources/dict/test-umls-dict.txt')
        generateDictFile(dictFile, ['C0334292', 'C0227391'].toSet())
    }
}
