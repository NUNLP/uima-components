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

package org.northshore.cbri.brat;

public class AttributeAnnotation extends BratAnnotation {

  private final String entityId;
  private final String value;
  
  protected AttributeAnnotation(String id, String type, String entityId, String value) {
    super(id, type);
    this.entityId = entityId;
    this.value = value;
  }
  
  public String getEntityId() {
    return this.entityId;
  }
  
  public String getValue() {
    return this.value;
  }
  
  @Override
  public String toString() {
    return super.toString() + " entityId:" + this.getEntityId() + " value:" + this.getValue();
  }
}
