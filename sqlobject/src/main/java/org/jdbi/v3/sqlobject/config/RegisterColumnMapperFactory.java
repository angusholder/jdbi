/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbi.v3.sqlobject.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jdbi.v3.core.extension.annotation.UseExtensionConfigurer;
import org.jdbi.v3.core.mapper.ColumnMapperFactory;
import org.jdbi.v3.sqlobject.config.internal.RegisterColumnMapperFactoryImpl;

/**
 * Registers a column mapper factory in the scope of a SQL Object type or method.
 */
@UseExtensionConfigurer(RegisterColumnMapperFactoryImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Repeatable(RegisterColumnMapperFactories.class)
public @interface RegisterColumnMapperFactory {
    /**
     * The column mapper factory class to register
     * @return the column mapper factory class
     */
    Class<? extends ColumnMapperFactory> value();
}
