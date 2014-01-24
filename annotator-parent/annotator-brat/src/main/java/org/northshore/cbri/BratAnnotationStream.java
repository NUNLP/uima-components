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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Span;

/**
 * Reads the annotations from the brat .ann annotation file.
 */
public class BratAnnotationStream implements ObjectStream<BratAnnotation> {
	
	public static final String SPAN_TYPE = "Span";
	public static final String RELATION_TYPE = "Relation";
	public static final String ATTRIBUTE_TYPE = "Attribute";

	static abstract class BratAnnotationParser {

		static final int ID_OFFSET = 0;
		static final int TYPE_OFFSET = 1;

		BratAnnotation parse(String values[]) throws IOException {
			return null;
		}

		protected int parseInt(String intString) throws InvalidFormatException {
			try {
				return Integer.parseInt(intString);
			} catch (NumberFormatException e) {
				throw new InvalidFormatException(e);
			}
		}
	}

	static class SpanAnnotationParser extends BratAnnotationParser {

		private static final int BEGIN_OFFSET = 2;
		private static final int END_OFFSET = 3;

		@Override
		BratAnnotation parse(String[] values) throws IOException {

			if (values.length > 4) {
				String type = values[BratAnnotationParser.TYPE_OFFSET];

				int endOffset = -1;

				for (int i = END_OFFSET; i < values.length; i++) {
					if (!values[i].contains(";")) {
						endOffset = parseInt(values[i]);
						break;
					}
				}

				return new SpanAnnotation(
						values[BratAnnotationParser.ID_OFFSET], type,
						new Span(parseInt(values[BEGIN_OFFSET]), endOffset,
								type), "");
			} else {
				throw new InvalidFormatException(
						"Line must have at least 5 fields");
			}
		}
	}

	static class RelationAnnotationParser extends BratAnnotationParser {

		private static final int ARG1_OFFSET = 2;
		private static final int ARG2_OFFSET = 3;

		private String parseArg(String arg) throws InvalidFormatException {
			if (arg.length() > 4) {
				return arg.substring(5).trim();
			} else {
				throw new InvalidFormatException("Failed to parse argument: "
						+ arg);
			}
		}

		@Override
		BratAnnotation parse(String[] values) throws IOException {
			return new RelationAnnotation(
					values[BratAnnotationParser.ID_OFFSET],
					values[BratAnnotationParser.TYPE_OFFSET],
					parseArg(values[ARG1_OFFSET]),
					parseArg(values[ARG2_OFFSET]));
		}
	}

	static class AttributeAnnotationParser extends BratAnnotationParser {

		private static final int ENTITY_ID_OFFSET = 2;
		private static final int VALUE_OFFSET = 3;

		@Override
		BratAnnotation parse(String[] values) throws IOException {
			return new AttributeAnnotation(
					values[BratAnnotationParser.ID_OFFSET],
					values[BratAnnotationParser.TYPE_OFFSET],
					values[ENTITY_ID_OFFSET], 
					(String) (values.length == 4 ? values[VALUE_OFFSET] : "true"));
		}
	}

	private final Map<String, BratAnnotationParser> parsers = new HashMap<String, BratAnnotationParser>();
	private final BufferedReader reader;
	BratAnnotationStream(InputStream in) {
		reader = new BufferedReader(new InputStreamReader(in,
				Charset.forName("UTF-8")));

		parsers.put(SPAN_TYPE,
				new SpanAnnotationParser());
		parsers.put(RELATION_TYPE,
				new RelationAnnotationParser());
		parsers.put(ATTRIBUTE_TYPE,
				new AttributeAnnotationParser());
	}

	public BratAnnotation read() throws IOException {

		String line = reader.readLine();

		if (line != null) {
			String values[] = WhitespaceTokenizer.INSTANCE.tokenize(line);

			if (values.length > 2) {
				String typeClass = null;
				String id = values[BratAnnotationParser.ID_OFFSET];
				switch (id.charAt(0)) {
				case 'T':
					typeClass = SPAN_TYPE;
					break;
				case 'A':
					typeClass = ATTRIBUTE_TYPE;
					break;
				case 'R':
					typeClass = RELATION_TYPE;
					break;
				default:
					throw new IOException("unkown annotation type");
				}

				BratAnnotationParser parser = parsers.get(typeClass);

				if (parser == null) {
					throw new IOException(
							"Failed to parse ann document with id " + id
									+ " type class, no parser registered: "
									+ values[BratAnnotationParser.TYPE_OFFSET]);
				}

				return parser.parse(values);
			}
		} else {
			return null;
		}

		return null;
	}

	public void reset() throws IOException, UnsupportedOperationException {
		reader.reset();
	}

	public void close() throws IOException {
		reader.close();
	}
}
