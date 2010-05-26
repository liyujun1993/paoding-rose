/*
 * Copyright 2007-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.paoding.rose.testcases.resource;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;
import net.paoding.rose.web.annotation.ReqMethod;
import net.paoding.rose.web.impl.mapping.MatchResult;
import net.paoding.rose.web.impl.mapping.EngineGroup;
import net.paoding.rose.web.impl.mapping.EngineGroupImpl;
import net.paoding.rose.web.impl.thread.Engine;
import net.paoding.rose.web.impl.thread.Rose;

/**
 * 
 * @author 王志亮 [qieqie.wang@gmail.com]
 * 
 */
public class EngineGroupTest extends TestCase {

    private Engine getEngine = new Engine() {

        public int compareTo(Engine o) {
            return 0;
        }

        @Override
        public int isAccepted(HttpServletRequest rose) {
            return 1;
        }

        @Override
        public Object execute(Rose rose, MatchResult mr) throws Throwable {
            return null;
        }

        @Override
        public void destroy() {

        }

        @Override
        public String toString() {
            return "get";
        }
    };

    private Engine postEngine = new Engine() {

        public int compareTo(Engine o) {
            return 0;
        }

        @Override
        public int isAccepted(HttpServletRequest rose) {
            return 1;
        }

        @Override
        public Object execute(Rose rose, MatchResult mr) throws Throwable {
            return null;
        }

        @Override
        public void destroy() {

        }

        @Override
        public String toString() {
            return "post";
        }
    };

    private Engine defEngine = new Engine() {

        public int compareTo(Engine o) {
            return 1;
        }

        @Override
        public int isAccepted(HttpServletRequest rose) {
            return 1;
        }

        @Override
        public Object execute(Rose rose, MatchResult mr) throws Throwable {
            return null;
        }

        @Override
        public void destroy() {

        }

        @Override
        public String toString() {
            return "def";
        }
    };

    public void testGetPost() {
        EngineGroup engineGroup = new EngineGroupImpl();
        engineGroup.addEngine(ReqMethod.GET, getEngine);
        engineGroup.addEngine(ReqMethod.POST, postEngine);

        assertSame(getEngine, engineGroup.getEngines(ReqMethod.GET)[0]);
        assertSame(postEngine, engineGroup.getEngines(ReqMethod.POST)[0]);

        String msg = "not allowed method should return engines with length is zero";
        assertEquals(msg, 0, engineGroup.getEngines(ReqMethod.PUT).length);
        assertEquals(msg, 0, engineGroup.getEngines(ReqMethod.DELETE).length);
        assertEquals(msg, 0, engineGroup.getEngines(ReqMethod.OPTIONS).length);

        assertEquals("[GET, POST]", engineGroup.toString());
    }

    public void testNotOverrideByAll() {
        EngineGroup engineGroup = new EngineGroupImpl();
        engineGroup.addEngine(ReqMethod.GET, getEngine);
        engineGroup.addEngine(ReqMethod.ALL, defEngine);
        engineGroup.addEngine(ReqMethod.POST, postEngine);

        assertSame(getEngine, engineGroup.getEngines(ReqMethod.GET)[0]);
        assertSame(defEngine, engineGroup.getEngines(ReqMethod.GET)[1]);
        assertSame(postEngine, engineGroup.getEngines(ReqMethod.POST)[0]);
        assertSame(defEngine, engineGroup.getEngines(ReqMethod.POST)[1]);

        assertSame(defEngine, engineGroup.getEngines(ReqMethod.PUT)[0]);
        assertSame(defEngine, engineGroup.getEngines(ReqMethod.DELETE)[0]);

        assertEquals("[GET, POST, DELETE, PUT, HEAD, OPTIONS, TRACE]", engineGroup.toString());
    }
}