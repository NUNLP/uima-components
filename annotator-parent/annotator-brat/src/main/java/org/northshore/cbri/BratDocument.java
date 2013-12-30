/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.northshore.cbri;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import opennlp.tools.util.ObjectStream;

public class BratDocument {

	private final String id;
	private final String text;
	private final Map<String, BratAnnotation> annotationMap;
	
	private Map<String, SpanAnnotation> spanAnnotations;
	private Map<String, AttributeAnnotation> attrAnnotations;
	private Map<String, RelationAnnotation> relAnnotations;

	public BratDocument(String id, String text,
			Collection<BratAnnotation> annotations) {
		
		this.spanAnnotations = null;
		this.attrAnnotations = null;
		this.relAnnotations = null;
		
		this.id = id;
		this.text = text;

		Map<String, BratAnnotation> annMap = new HashMap<String, BratAnnotation>();
		for (BratAnnotation annotation : annotations) {
			annMap.put(annotation.getId(), annotation);
		}

		this.annotationMap = Collections.unmodifiableMap(annMap);
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public BratAnnotation getAnnotation(String id) {
		return annotationMap.get(id);
	}

	public Collection<BratAnnotation> getAnnotations() {
		return annotationMap.values();
	}
	
	public final Map<String, SpanAnnotation> getSpanAnnotations() {
		if (this.spanAnnotations == null) {
			this.spanAnnotations = new HashMap<>();
			for (String key : annotationMap.keySet()) {
				BratAnnotation ann = annotationMap.get(key);
				if (ann instanceof SpanAnnotation) {
					this.spanAnnotations.put(key, (SpanAnnotation) ann);
				}
			}
		}
		return  Collections.unmodifiableMap(this.spanAnnotations);
	}
	
	public final Map<String, AttributeAnnotation> getAttrAnnotations() {
		if (this.attrAnnotations == null) {
			this.attrAnnotations = new HashMap<>();
			for (String key : annotationMap.keySet()) {
				BratAnnotation ann = annotationMap.get(key);
				if (ann instanceof AttributeAnnotation) {
					this.attrAnnotations.put(key, (AttributeAnnotation) ann);
				}
			}
		}
		return  Collections.unmodifiableMap(this.attrAnnotations);
	}

	public final Map<String, RelationAnnotation> getRelAnnotations() {
		if (this.relAnnotations == null) {
			this.relAnnotations = new HashMap<>();
			for (String key : annotationMap.keySet()) {
				BratAnnotation ann = annotationMap.get(key);
				if (ann instanceof RelationAnnotation) {
					this.relAnnotations.put(key, (RelationAnnotation) ann);
				}
			}
		}
		return  Collections.unmodifiableMap(this.relAnnotations);
	}


	public static BratDocument parseDocument(String id, InputStream txtIn,
			InputStream annIn) throws IOException {

		Reader txtReader = new InputStreamReader(txtIn,
				Charset.forName("UTF-8"));

		StringBuilder text = new StringBuilder();

		char cbuf[] = new char[1024];

		int len;
		while ((len = txtReader.read(cbuf)) > 0) {
			text.append(cbuf, 0, len);
		}

		Collection<BratAnnotation> annotations = new ArrayList<BratAnnotation>();

		ObjectStream<BratAnnotation> annStream = new BratAnnotationStream(id,
				annIn);

		BratAnnotation ann;
		while ((ann = annStream.read()) != null) {
			annotations.add(ann);
		}

		return new BratDocument(id, text.toString(), annotations);
	}
}
