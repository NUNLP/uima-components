import static org.northshore.cbri.UIMAUtil.*
import org.apache.ctakes.typesystem.type.textspan.Segment

segs = select(type:Segment, filter:{ it.id in ['FINAL_DIAGNOSIS'] })

