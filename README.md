# quick-park-assist

Java based parking spot management application

# SonarQube Integration Guide

### Steps to Integrate SonarQube with IntelliJ IDEA and Analyze a Project

---

#### **Step 1: Download SonarQube from the Official Website**

1. Visit the official SonarQube website: [https://www.sonarqube.org/downloads/](https://www.sonarqube.org/downloads/).
2. Scroll down to the **Community Edition (free)** section and click on the **Download** button.
3. The download should start automatically. You will get a `.zip` file for the SonarQube server.

---

#### **Step 2: Extract SonarQube**

1. Once the download is complete, extract the `.zip` file to a directory of your choice.  
   For example:
   - On Windows: `C:\SonarQube`
   - On Linux/macOS: `/opt/sonarqube`

---

#### **Step 3: Start the SonarQube Server**

** NOTE: Java Version: SonarQube requires Java 11 or higher. Set the JAVA_HOME environment variable to point to your JDK.**

1. Open a terminal (or Command Prompt) and navigate to the `bin` directory inside your SonarQube installation directory.
   - On Windows: Navigate to `C:\SonarQube\bin\windows-x86-64`.
   - On Linux/macOS: Navigate to `/opt/sonarqube/bin/linux-x86-64`.
2. Start the SonarQube server:
   - On Windows: Run `StartSonar.bat`.
   - On Linux/macOS: Run `./sonar.sh start`.
3. Wait for the server to start. You should see log messages indicating that SonarQube is running.

---

#### **Step 4: Access the SonarQube Web Interface**

1. Open a web browser and navigate to [http://localhost:9000](http://localhost:9000).
2. Log in using the default credentials:
   - Username: `admin`
   - Password: `admin`
3. Update the default password if prompted.

---

#### **Step 5: Install the SonarQube Plugin in IntelliJ IDEA**

1. Open IntelliJ IDEA.
2. Go to **File** > **Settings** (or **Preferences** on macOS).
3. In the left-hand panel, select **Plugins**.
4. Search for **SonarQube** in the search bar and press **Enter**.
5. Find the **SonarQube** plugin in the search results and click **Install**.
6. Restart IntelliJ IDEA after installation.

---

#### **Step 6: Create a Project on SonarQube Manually**

1. **Log in to the SonarQube Server**:
   - Open your browser and go to [http://localhost:9000](http://localhost:9000).
   - Log in using your credentials. Default credentials are:
     - Username: `admin`
     - Password: `admin` (if changed, use your updated password).
2. **Go to the Projects Section**:
   - From the SonarQube Dashboard, click on the **Projects** tab in the top menu bar.
   - Click the **Create Project** button (located at the top-right corner of the page).
3. **Select the Manual Setup Option**:
   - Choose **Manually** to create a project manually.
4. **Enter Project Details**:
   - Provide the following information:
     - **Project Name**: Enter a descriptive name for your project (e.g., "My Java Project").
     - **Project Key**: Enter a unique identifier for your project (e.g., `my_java_project`).
   - Click the **Set Up** button.
5. **Choose Analysis Method**:
   - When prompted, select **Locally**.
6. **Generate Authentication Token**:
   - Enter a name for the token (e.g., "MyProjectToken") and click **Generate Token**.
   - Copy the generated token immediately.
   - Click **Continue**.
7. **Select Build Tool and Generate Command**:
   - Select your preferred build tool (e.g., Maven, Gradle). Choose **Maven** for this setup.
   - SonarQube will display a command template. For Maven, it will look like this:
     ```bash
     mvn sonar:sonar \
       -Dsonar.projectKey=my_java_project \
       -Dsonar.host.url=http://localhost:9000 \
       -Dsonar.login=<your_generated_token>
     ```
   - Replace `<your_generated_token>` with the token generated earlier.
8. **Run the Command**:
   - Open a terminal (or Command Prompt) and navigate to the root directory of your project.
   - Paste and execute the generated command.
   - If the build is successful, the project will appear on the SonarQube dashboard.

---

#### **Step 7: Configure SonarQube Server in IntelliJ IDEA**

1. Open IntelliJ IDEA and navigate to your project.
2. Go to **File** > **Settings** (or **Preferences** on macOS).
3. In the left-hand menu, select **Tools** > **SonarQube**.
4. Click the **+** icon to add a new SonarQube server.
5. Fill in the following details:
   - **Name**: Enter a name for the server configuration (e.g., "My SonarQube Server").
   - **Server URL**: Enter the URL of your SonarQube server (e.g., `http://localhost:9000`).
   - **Authentication Token**: Paste the token generated in Step 6.
6. Click **Test Connection** to verify the setup.
7. Click **OK** to save.

---

#### **Step 8: View Results in SonarQube Dashboard**

1. Open your browser and navigate to [http://localhost:9000](http://localhost:9000).
2. Go to your project in the dashboard to view the analysis results, including issues, code smells, and quality gates.

---

By following these steps, you will successfully integrate SonarQube with IntelliJ IDEA and analyze your project for code quality.
