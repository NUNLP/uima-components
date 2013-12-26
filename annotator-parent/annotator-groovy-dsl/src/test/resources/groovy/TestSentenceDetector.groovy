import static org.northshore.cbri.UIMAUtil.*

import java.util.regex.Matcher

import org.apache.ctakes.typesystem.type.textspan.Segment
import org.apache.ctakes.typesystem.type.textspan.Sentence

select(type:Segment).each { seg ->
	Matcher m = (seg.coveredText =~ /([A-Z].+\.)/)
	m.each {
		println m.group(1)
		create(type:Sentence, begin:m.start(1), end:m.end(1))
	}
}
