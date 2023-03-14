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
package org.jdbi.v3.sqlobject.statement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jdbi.v3.core.extension.annotation.UseExtensionHandler;
import org.jdbi.v3.sqlobject.SqlObjectFactory;
import org.jdbi.v3.sqlobject.statement.internal.SqlQueryHandler;

/**
 * Used to indicate that a method should execute a query.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@UseExtensionHandler(id = SqlObjectFactory.EXTENSION_ID, value = SqlQueryHandler.class)
public @interface SqlQuery {

    /**
     * The query (or query name if using a statement locator) to be executed. If no value is specified,
     * the value will be the method name of the method being annotated. This is only useful
     * in conjunction with a statement locator.
     *
     * @return the SQL string (or name)
     */
    String value() default "";
}
