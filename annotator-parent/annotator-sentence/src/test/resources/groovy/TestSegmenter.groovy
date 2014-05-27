import static org.northshore.cbri.dsl.UIMAUtil.*

import java.util.regex.Pattern

import org.apache.ctakes.typesystem.type.textspan.Segment

// Match segment headings
matcher = jcas.documentText =~ ~/(?m)(?s)FINAL DIAGNOSIS:(.+)COMMENT:/
matcher.each {
    create(type:Segment, begin:matcher.start(1), end:matcher.end(1), id:'FINAL_DIAGNOSIS')
}
matcher = jcas.documentText =~ ~/(?m)(?s)COMMENT:(.+)\Z/
matcher.each {
    create(type:Segment, begin:matcher.start(1), end:matcher.end(1), id:'COMMENT')
}
