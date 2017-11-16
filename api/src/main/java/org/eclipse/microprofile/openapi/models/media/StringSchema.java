/**
 * Copyright (c) 2017 Contributors to the Eclipse Foundation
 * Copyright 2017 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eclipse.microprofile.openapi.models.media;

import java.util.List;

/**
 * The schema used for an object that holds a string.
 */
public interface StringSchema extends Schema<String> {

    /**
     * Change this StringSchema's type property from the default value to the given string.
     *
     * @param type the name of a valid type
     * @return the current StringSchema instance
     */
    StringSchema type(String type);

    /**
     * Sets the default property of this StringSchema to the given default value.
     * 
     * @param defaultValue a value to use as the default
     * @return the current StringSchema instance
     */
    StringSchema defaultValue(String defaultValue);

    /**
     * Sets the enumerated list of values allowed for objects defined by this schema.
     *
     * @param enumeration a list of values allowed
     * @return the current StringSchema instance
     */
    StringSchema enumeration(List<String> enumeration);

    /**
     * Adds an item of the appropriate type to the enumerated list of values allowed.
     *
     * @param enumerationItem an object to add to the enumerated values
     * @return the current StringSchema instance
     */
    StringSchema addEnumerationItem(String enumerationItem);

}