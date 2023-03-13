package io.openliberty.tools.intellij.it;

import com.intellij.remoterobot.RemoteRobot;
import io.openliberty.tools.intellij.it.fixtures.WelcomeFrameFixture;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitForIgnoringError;

public abstract class SingleModLibertyLSTestCommon {
    public static final String REMOTEBOT_URL = "http://localhost:8082";
    public static final RemoteRobot remoteRobot = new RemoteRobot(REMOTEBOT_URL);

    public static String projectName;
    String projectPath;
    String wlpInstallPath;
    String appBaseURL;
    String appExpectedOutput;


    public SingleModLibertyLSTestCommon(String projectName, String projectPath, String wlpInstallPath, String appBaseURL, String appExpectedOutput) {
        this.projectName = projectName;
        this.projectPath = projectPath;
        this.wlpInstallPath = wlpInstallPath;
        this.appBaseURL = appBaseURL;
        this.appExpectedOutput = appExpectedOutput;
    }

    @BeforeEach
    public void beforeEach(TestInfo info) {
        System.out.println(
                "INFO: Test " + this.getClass().getSimpleName() + "#" + info.getDisplayName() + " entry: " + java.time.LocalDateTime.now());
    }

    @AfterEach
    public void afterEach(TestInfo info) {
        System.out.println(
                "INFO: Test " + this.getClass().getSimpleName() + "#" + info.getDisplayName() + " exit: " + java.time.LocalDateTime.now());
    }

    @AfterAll
    public static
    void cleanup() {
        UIBotTestUtils.closeDashboardView(remoteRobot);
        UIBotTestUtils.closeProjectView(remoteRobot);
        UIBotTestUtils.closeProjectFrame(remoteRobot);
    }

    @Test
    public void testServerXMLFeatureHover() {
        String testName = "testServerXMLFeatureHover";
        String testHoverTarget = "mpHealth-4.0";
        String testAppName = "gradle-app";
        String absoluteWLPPath = projectPath + wlpInstallPath;
        String hoverExpectedOutcome = "This feature provides support for the MicroProfile Health specification.";

        // open server.xml file
        UIBotTestUtils.openServerXMLFile(remoteRobot, projectName);

        //mover cursor to hover point
        UIBotTestUtils.featureHoverInGradleAppServerXML(remoteRobot, testAppName, testHoverTarget);

        try {
            // Validate that the hover action raised the expected hint text
            TestUtils.validateHoverAction(remoteRobot, hoverExpectedOutcome);
        } finally {
            // clean up?
            // close server.xml file
            UIBotTestUtils.closeSourceFile(remoteRobot, "server.xml");
        }
    }

    public static void prepareEnv(String projectPath, String projectName) {
        waitForIgnoringError(Duration.ofMinutes(4), Duration.ofSeconds(5), "Wait for IDE to start", "IDE did not start", () -> remoteRobot.callJs("true"));
        remoteRobot.find(WelcomeFrameFixture.class, Duration.ofMinutes(2));

        UIBotTestUtils.importProject(remoteRobot, projectPath, projectName);
        UIBotTestUtils.openProjectView(remoteRobot);
        UIBotTestUtils.openDashboardView(remoteRobot);
        UIBotTestUtils.validateDashboardItemIsShowing(remoteRobot, projectName);
        UIBotTestUtils.expandDashboardProjectTree(remoteRobot);

        //UIBotTestUtils.openServerXMLFile(remoteRobot, projectName);
    }


}
