//
// ============================================================================
// (C) Copyright Schalk W. Cronje 2017
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and limitations under the License.
//
// ============================================================================
//

package org.ysb33r.gradle.nodejs.impl

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.ysb33r.gradle.olifant.AbstractDistributionInstaller
import org.ysb33r.gradle.olifant.OperatingSystem
import static org.ysb33r.gradle.olifant.OperatingSystem.Arch.*

/** Downloads specific versions of NodeJS.
 * Currently limited to Windows (x86, x86_64), MacOS, Linux (x86, x86_64).
 * 
 * <p> There are more
 * binary packages are available from the NodeJS site, but currently these are not being tested not implemented.
 * This includes:
 * 
 * <ul>
 *    <li> linux-armv6l.tar.xz
 *    <li> linux-armv7l.tar.xz
 *    <li> linux-arm64.tar.xz
 *    <li> linux-ppc64le.tar.xz
 *    <li> linux-ppc64.tar.xz
 *    <li> linux-s390x.tar.xz
 * </ul>
 * <p> (Patches welcome!)
 */
@CompileStatic
class Downloader extends AbstractDistributionInstaller {
    static final OperatingSystem OS = OperatingSystem.current()
    static final OperatingSystem.Arch ARCH = OS.getArch()

    static String baseURI = System.getProperty('org.ysb33r.gradle.nodejs.uri') ?: 'https://nodejs.org/dist'


    Downloader(final String version,final Project project) {
        super('nodejs',version,'native-binaries/nodejs',project)
    }

    /** Provides an appropriate URI to download a specific tag of NodeJS.
     *
     * @param ver Version of Doxygen to download
     * @return URI for a supported platform; {@code null} otherwise.
     */
    @Override
    URI uriFromVersion(final String ver) {
        String variant
        if(OS.windows) {
            if(OS.arch == X86) {
                variant = 'win-x86.zip'
            } else {
                variant = 'win-x64.zip'
            }
        } else if(OS.linux) {
            String arch
            switch(ARCH) {
                case X86_64:
                    arch = 'x64'
                    break
                case X86:
                    arch = 'x86'
                    break
            }
            if(arch) {
                variant = "linux-${arch}.tar.xz"
            }
            
        } else if(OS.macOsX) {
            variant='darwin-x64.tar.gz'
        }

        variant ? "${baseURI}/v${ver}/node-v${ver}-${variant}".toURI() : null
    }

    /** Returns the path to the {@code node} executable.
     * Will force a download if not already downloaded.
     *
     * @return Location of {@code node} or null if not a supported operating system.
     */
    File getNodeExecutablePath() {
        File root = getDistributionRoot()
        if(root == null) {
            return null
        } else if(OS.windows) {
            new File(root,'node.exe')
        } else {
            new File(root,'bin/node')
        }
    }

    /** Returns the path to the included {@code npm-cli.js} Node.js executable JavaScript.
     * Will force a download if not already downloaded.
     *
     * @return Location of {@code npm-vli.js} or null if not a supported operating system.
     */
    File getNpmExecutablePath() {
        File root = getDistributionRoot()
        if(root == null) {
            return null
        } else if(OS.windows) {
            new File(root,'node_modules/npm/bin/npm-cli.js')
        } else {
            new File(root,'../lib/node_modules/npm/bin/npm-cli.js')
        }
    }
}

