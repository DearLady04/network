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
package com.flowpowered.networking.process;

import com.flowpowered.networking.Message;

/**
 * This interface is used to mark messages that are used to setup the ChannelProcessor.
 */
public interface ProcessorSetupMessage extends Message {
    /*
     * Gets the processor to use to process messages subsequent to this one. Called after the Message has been encoded to set the ChannelProcessor for subsequent messages.
     *
     * @return the new ChannelProcessor or null for none
     */
    public ChannelProcessor getProcessor();

    /**
     * Called by {@link DecodingProcessorHandler} to indicate the {@link DecodingProcessorHandler} instance to use. Called post-decode.
     *
     * @param handler the new handler
     */
    public void setDecodingProcessorHandler(DecodingProcessorHandler handler);
}
