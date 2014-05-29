package org.northshore.cbri.dsl

import groovy.util.logging.Log4j

import org.apache.uima.UimaContext
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceInitializationException
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

import com.google.common.base.Charsets
import com.google.common.io.Files
import com.google.common.io.Resources

@Log4j
public class GroovyAnnotator extends org.apache.uima.fit.component.JCasAnnotator_ImplBase {

    public static final String PARAM_SCRIPT_FILE = "scriptFileName"
    public static final String PARAM_SCRIPT_DIR_ENV_VAR = "scriptDirEnvVar"

    @ConfigurationParameter(name = "scriptFileName", mandatory = true, description = "File holding Groovy script")
    private String scriptFileName

    @ConfigurationParameter(name = "scriptDirEnvVar", mandatory = false, description = "Environment variable that encodes the directory holding the Groovy script")
    private String scriptDirEnvVar
    
    private Script script = null

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context)
        CompilerConfiguration config = new CompilerConfiguration()
        
        // TODO: add all type imports here
        def icz = new ImportCustomizer()
        icz.addImports('org.apache.ctakes.typesystem.type.textspan.Sentence')
        config.addCompilationCustomizers(icz)
        
        config.setScriptBaseClass("org.northshore.cbri.dsl.UIMAUtil")
        
        GroovyShell shell = new GroovyShell(config)
        try {
            String scriptContents = ""
            if (this.scriptDirEnvVar != null) {
                String path = System.getenv(this.scriptDirEnvVar)
                if (path != null) {
                    File scriptFile = new File(path + "/" + this.scriptFileName)
                    log.info "Loading groovy script file: ${scriptFile.toString()}"
                    scriptContents = Files.toString(scriptFile, Charsets.UTF_8)
                }
            }
            else {
                log.info "Loading groovy script file: ${this.scriptFileName}"
                URL url = Resources.getResource(this.scriptFileName)
                scriptContents = Resources.toString(url, Charsets.UTF_8)
            }
            this.script = shell.parse(scriptContents)
        } catch (IOException e) {
            throw new ResourceInitializationException()
        }
    }

    @Override
    void process(JCas jcas) {
        UIMAUtil.setJCas(jcas)
        this.script.run()
    }
}
