package org.northshore.cbri;

import org.northshore.cbri.UIMAUtil;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.apache.uima.fit.descriptor.ConfigurationParameter;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

public class GroovyAnnotator extends org.apache.uima.fit.component.JCasAnnotator_ImplBase {

    public static final String PARAM_SCRIPT_FILE = "scriptFileName";
    public static final String PARAM_SCRIPT_DIR_ENV_VAR = "scriptDirEnvVar";

    @ConfigurationParameter(name = PARAM_SCRIPT_FILE, mandatory = true, description = "File holding Groovy script")
    private String scriptFileName;

    @ConfigurationParameter(name = PARAM_SCRIPT_DIR_ENV_VAR, mandatory = false, description = "Environment variable that encodes the directory holding the Groovy script")
    private String scriptDirEnvVar;
    
    private Script script = null;

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass("org.northshore.cbri.UIMAUtil");
        GroovyShell shell = new GroovyShell(config);
        try {
            String scriptContents = "";
            if (this.scriptDirEnvVar != null) {
                String path = System.getenv(this.scriptDirEnvVar);
                if (path != null) {
                    File scriptFile = new File(path + "/" + this.scriptFileName);
                    System.out.println("Loading script file: " + scriptFile.toString());
                    scriptContents = Files.toString(scriptFile, Charsets.UTF_8);
                }
            }
            else {
                System.out.println("GroovyAnnotator loading script file: " + this.scriptFileName);
                URL url = Resources.getResource(this.scriptFileName);
                scriptContents = Resources.toString(url, Charsets.UTF_8);
            }
            this.script = shell.parse(scriptContents);
        } catch (IOException e) {
            throw new ResourceInitializationException();
        }
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        UIMAUtil.setJCas(jcas);
        this.script.run();
    }
}
