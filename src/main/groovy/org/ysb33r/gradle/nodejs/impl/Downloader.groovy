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

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.util.GradleVersion
import org.ysb33r.gradle.olifant.AbstractDistributionInstaller
import org.ysb33r.gradle.olifant.DistributionFailedException
import org.ysb33r.gradle.olifant.OperatingSystem
import static org.ysb33r.gradle.olifant.OperatingSystem.Arch.*

/** Downloads specific versions of NodeJS.
 * Currently limited to Windows (x86, x86_64), MacOS, Linux (x86, x86_64, ARMv6-8). There are more
 * binary packages are available from the NodeJS site, but curretnly these are not being tested. (Patches welcome!)
 */
@CompileStatic
class Downloader extends AbstractDistributionInstaller {
    static final OperatingSystem OS = OperatingSystem.current()
    static final OperatingSystem.Arch ARCH = OS.getArch()

    String baseURI = 'https://nodejs.org/dist'


    Downloader(final String version,final Project project) {
        super('nodejs',version,'native-binaries/nodejs',project)
    }

    /** Provides an appropriate URI to download a specific version of NodeJS.
     *
     * @param ver Version of Doxygen to download
     * @return URI for a supported platform; {@code null} otherwise.
     */
    @Override
    URI uriFromVersion(final String ver) {
        String variant
        if(OS.windows) {
            if(OS.arch == X86_64) {
                variant = 'win-x64.zip'
            } else {
                variant = 'win-86.zip'
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
//            'linux-armv6l.tar.xz'
//            'linux-armv7l.tar.xz'
//            'linux-arm64.tar.xz'
//            'linux-ppc64le.tar.xz'
//            'linux-ppc64.tar.xz'
//            'linux-s390x.tar.xz'
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

    /** Returns the path to the included {@code npm} executable.
     * Will force a download if not already downloaded.
     *
     * @return Location of {@code npm} or null if not a supported operating system.
     */
    File getNpmExecutablePath() {
        File root = getDistributionRoot()
        if(root == null) {
            return null
        } else if(OS.windows) {
            new File(root,'npm.cmd')
        } else {
            new File(root,'bin/npm')
        }
    }

//    /** Adds a special verification case for Doxygen Windows binaries.
//     *
//     * The Windows distribution is not zipped into a subfolder and needs extra care.
//     * For Linux & MacOsX it will use the default implementation.
//     *
//     * @param distDir Directory where distribution was unpacked to.
//     * @param distributionDescription A human-readable description
//     * @return Distribution directory
//     */
//    @Override
//    protected File getAndVerifyDistributionRoot(File distDir, String distributionDescription) {
//        if(OS.windows) {
//            if (!new File(distDir,'doxygen.exe').exists()) {
//                throw new DistributionFailedException("Doxygen '${distributionDescription}' does not contain 'doxygen.exe'.")
//            }
//            return distDir
//        } else {
//            super.getAndVerifyDistributionRoot(distDir, distributionDescription)
//        }
//    }
}

