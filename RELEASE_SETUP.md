# Release Repository Setup Guide

This guide explains how to set up release repositories for the Bean-Centric Testing Framework project.

## Current Configuration

The project is configured to use **GitHub Packages** as the distribution repository. This is set up in the `pom.xml`:

```xml
<distributionManagement>
    <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>https://maven.pkg.github.com/jamesbognar/bean-centric-testing</url>
    </repository>
    <snapshotRepository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>https://maven.pkg.github.com/jamesbognar/bean-centric-testing</url>
    </snapshotRepository>
</distributionManagement>
```

## GitHub Packages Setup

### 1. Enable GitHub Packages

GitHub Packages is automatically available for your repository. No additional setup is required.

### 2. Authentication

To publish packages, you'll need a GitHub Personal Access Token with the `write:packages` permission.

#### Create a Personal Access Token:

1. Go to GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Click "Generate new token (classic)"
3. Select the `write:packages` scope
4. Copy the generated token

#### Configure Maven Authentication:

Create a `~/.m2/settings.xml` file (or use the provided `settings.xml.example`):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 
          http://maven.apache.org/xsd/settings-1.0.0.xsd">
    
    <servers>
        <server>
            <id>github</id>
            <username>jamesbognar</username>
            <password>YOUR_GITHUB_TOKEN_HERE</password>
        </server>
    </servers>
    
</settings>
```

### 3. Publishing Releases

#### Manual Release:

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Deploy to GitHub Packages
mvn deploy
```

#### Automated Release with GitHub Actions:

Create `.github/workflows/release.yml`:

```yaml
name: Release

on:
  push:
    tags:
      - 'v*'

jobs:
  release:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Cache Maven dependencies
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
        restore-keys: ${{ runner.os }}-m2
    
    - name: Deploy to GitHub Packages
      run: mvn deploy
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

## Alternative Repository Options

### 1. Maven Central (Sonatype OSSRH)

For publishing to Maven Central, you'll need to:

1. **Create a Sonatype account** at https://issues.sonatype.org/
2. **Request namespace approval** for `org.bct`
3. **Update distributionManagement**:

```xml
<distributionManagement>
    <snapshotRepository>
        <id>ossrh</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </snapshotRepository>
    <repository>
        <id>ossrh</id>
        <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
    </repository>
</distributionManagement>
```

4. **Add GPG signing** (already configured in your `pom.xml` release profile)

### 2. Local Repository

For development/testing only:

```xml
<distributionManagement>
    <repository>
        <id>local-repo</id>
        <url>file://${project.basedir}/target/repo</url>
    </repository>
</distributionManagement>
```

## Using Published Packages

### From GitHub Packages:

Add to your project's `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>https://maven.pkg.github.com/jamesbognar/bean-centric-testing</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.bct</groupId>
        <artifactId>bean-centric-testing-framework</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### From Maven Central:

```xml
<dependencies>
    <dependency>
        <groupId>org.bct</groupId>
        <artifactId>bean-centric-testing-framework</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## Release Process

### 1. Prepare Release

```bash
# Update version in pom.xml (remove -SNAPSHOT)
# Update CHANGELOG.md
# Commit changes
git add .
git commit -m "Prepare release 1.0.0"
```

### 2. Create Release Tag

```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

### 3. Deploy

```bash
# Manual deployment
mvn clean deploy

# Or let GitHub Actions handle it automatically
```

### 4. Post-Release

```bash
# Update version to next snapshot
# Update CHANGELOG.md
# Commit and push
```

## Troubleshooting

### Common Issues:

1. **Authentication Failed**: Check your GitHub token has `write:packages` permission
2. **Repository Not Found**: Ensure the repository name matches exactly
3. **Permission Denied**: Verify your GitHub token is valid and not expired
4. **Site Generation Fails**: The distribution management configuration should resolve this

### GitHub Packages Limitations:

- **Visibility**: Packages inherit repository visibility (public repo = public packages)
- **Storage**: Limited by GitHub repository storage limits
- **Retention**: Packages are retained as long as the repository exists

### Maven Central Benefits:

- **Wide Availability**: Available in all Maven repositories by default
- **No Authentication**: Users don't need special configuration
- **Long-term Storage**: Permanent storage and availability
- **Professional Standard**: Industry standard for Java libraries

## Recommendations

For the Bean-Centric Testing Framework, I recommend:

1. **Start with GitHub Packages** for initial releases and testing
2. **Consider Maven Central** for wider adoption once the project matures
3. **Use automated releases** with GitHub Actions for consistency
4. **Maintain semantic versioning** (e.g., 1.0.0, 1.0.1, 1.1.0)

The current configuration should resolve your Maven site generation error while providing a solid foundation for future releases.
