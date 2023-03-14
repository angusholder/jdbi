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
package org.jdbi.v3.sqlobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.extension.ConfigCustomizerFactory;
import org.jdbi.v3.core.extension.ExtensionFactory;
import org.jdbi.v3.core.extension.ExtensionHandler;
import org.jdbi.v3.core.extension.ExtensionHandler.ExtensionHandlerFactory;
import org.jdbi.v3.core.extension.ExtensionHandlerCustomizer;
import org.jdbi.v3.core.extension.ExtensionMetadata;
import org.jdbi.v3.core.internal.JdbiClassUtils;

abstract class AbstractSqlObjectFactory implements ExtensionFactory {

    private static final ExtensionHandler WITH_HANDLE_HANDLER = (handleSupplier, target, args) ->
            ((HandleCallback<?, RuntimeException>) args[0]).withHandle(handleSupplier.getHandle());
    private static final ExtensionHandler GET_HANDLE_HANDLER = (handleSupplier, target, args) -> handleSupplier.getHandle();

    @Override
    public void buildExtensionInitData(ExtensionMetadata.Builder builder) {
        final Class<?> extensionType = builder.getExtensionType();

        ExtensionHandler toStringHandler = (handlerSupplier, target, args) ->
                "Jdbi sqlobject proxy for " + extensionType.getName() + "@" + Integer.toHexString(target.hashCode());
        JdbiClassUtils.safeMethodLookup(Object.class, "toString").ifPresent(m -> builder.addMethodHandler(m, toStringHandler));

        JdbiClassUtils.safeMethodLookup(SqlObject.class, "getHandle").ifPresent(m -> builder.addMethodHandler(m, GET_HANDLE_HANDLER));
        JdbiClassUtils.safeMethodLookup(SqlObject.class, "withHandle", HandleCallback.class)
                .ifPresent(m -> builder.addMethodHandler(m, WITH_HANDLE_HANDLER));
    }

    @Override
    public Collection<ExtensionHandlerCustomizer> getExtensionHandlerCustomizers(ConfigRegistry config) {
        final HandlerDecorators handlerDecorators = config.get(HandlerDecorators.class);
        return Collections.singleton(handlerDecorators::customize);
    }

    @Override
    public Collection<ExtensionHandlerFactory> getExtensionHandlerFactories(ConfigRegistry config) {
        final Handlers handlers = config.get(Handlers.class);
        List<ExtensionHandlerFactory> factories = new ArrayList<>();

        factories.add(new SqlMethodHandlerFactory());
        factories.addAll(handlers.getFactories());
        return Collections.unmodifiableList(factories);
    }

    @Override
    public Collection<ConfigCustomizerFactory> getConfigCustomizerFactories(ConfigRegistry config) {
        return Collections.singleton(SqlObjectCustomizerFactory.FACTORY);
    }

    static boolean isConcrete(Class<?> extensionTypeClass) {
        return extensionTypeClass.getAnnotation(GenerateSqlObject.class) != null;
    }
}
