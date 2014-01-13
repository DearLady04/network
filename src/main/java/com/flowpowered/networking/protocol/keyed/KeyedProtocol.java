/*
 * This file is part of Flow Networking, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Spout LLC <http://www.spout.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.flowpowered.networking.protocol.keyed;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.Message;
import com.flowpowered.networking.MessageHandler;
import com.flowpowered.networking.protocol.AbstractProtocol;
import com.flowpowered.networking.service.CodecLookupService;
import com.flowpowered.networking.service.HandlerLookupService;

import org.slf4j.Logger;

/**
 * A {@code AbstractProtocol} stores {@link Message}s and their respective {@link Codec}s and {@link MessageHandler}s by key.
 */
public abstract class KeyedProtocol extends AbstractProtocol {
    private final int maxPackets;
    private final ConcurrentMap<String, CodecLookupService> codecLookup;
    private final ConcurrentMap<String, HandlerLookupService> handlerLookup;

    public KeyedProtocol(String name, int defaultPort, int maxPackets) {
        super(name, defaultPort);
        this.maxPackets = maxPackets;
        codecLookup = new ConcurrentHashMap<>();
        handlerLookup = new ConcurrentHashMap<>();
    }

    /**
     * @param name
     * @param defaultPort
     * @param maxPackets this is one more than the maximum packet id
     * @param logger
     */
    public KeyedProtocol(String name, int defaultPort, int maxPackets, Logger logger) {
        super(name, defaultPort, logger);
        this.maxPackets = maxPackets;
        codecLookup = new ConcurrentHashMap<>();
        handlerLookup = new ConcurrentHashMap<>();
    }

    /**
     * Gets the handler lookup service associated with this AbstractProtocol
     *
     * @return the handler lookup service, or null if none
     */
    protected HandlerLookupService getHandlerLookupService(String key) {
        return handlerLookup.get(key);
    }

    /**
     * Gets the codec lookup service associated with this AbstractProtocol
     *
     * @return the codec lookup service, or null if none
     */
    protected CodecLookupService getCodecLookupService(String key) {
        return codecLookup.get(key);
    }

    public <M extends Message, C extends Codec<M>, H extends MessageHandler<M>> C registerMessage(String key, Class<C> codec, Class<H> handler) {
        try {
            CodecLookupService codecLookup = this.codecLookup.get(key);
            if (codecLookup == null) {
                codecLookup = new CodecLookupService(maxPackets);
                this.codecLookup.put(key, codecLookup);
            }
            HandlerLookupService handlerLookup = this.handlerLookup.get(key);
            if (handlerLookup == null) {
                handlerLookup = new HandlerLookupService();
                this.handlerLookup.put(key, handlerLookup);
            }
            C bind = codecLookup.bind(codec);
            if (bind != null && handler != null) {
                handlerLookup.bind(bind.getMessage(), handler);
            }
            return bind;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            getLogger().error("Error registering codec " + codec + ": ", e);  // TODO: Use parametrized message instead of string concatation.
            return null;
        }
    }

}