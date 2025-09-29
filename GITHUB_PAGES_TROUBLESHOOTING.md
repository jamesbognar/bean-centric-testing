# GitHub Pages Troubleshooting Guide

## Current Issue: 404 Error

You're getting a 404 error when visiting `https://jamesbognar.github.io/bean-centric-testing/`. Here's how to fix it:

## Step 1: Check GitHub Pages Settings

1. **Go to your repository**: https://github.com/jamesbognar/bean-centric-testing
2. **Click on "Settings"** (top menu bar)
3. **Scroll down to "Pages"** (left sidebar)
4. **Check the source setting**:
   - Should be set to **"GitHub Actions"** (not "Deploy from a branch")
   - If it's set to a branch, change it to "GitHub Actions"

## Step 2: Check Workflow Status

1. **Go to the "Actions" tab** in your repository
2. **Look for the latest workflow run**
3. **Check if it completed successfully**:
   - ✅ Green checkmark = Success
   - ❌ Red X = Failed
   - 🟡 Yellow circle = In progress

## Step 3: Check Workflow Logs

If the workflow failed:

1. **Click on the failed workflow run**
2. **Click on the job name** (e.g., "build-and-deploy")
3. **Look for error messages** in the logs
4. **Common issues**:
   - Maven build failures
   - Permission issues
   - Missing dependencies

## Step 4: Verify Site Generation

The workflow should generate files in `target/site/`. Check if these files exist:

- `target/site/index.html` (main page)
- `target/site/apidocs/` (Javadoc directory)
- `target/site/project-reports.html` (project reports)

## Step 5: Manual Testing

You can test the site generation locally:

```bash
# Clean and generate site
mvn clean site -DskipTests

# Check if files were generated
ls -la target/site/

# Start a local server to test
cd target/site
python3 -m http.server 8000
# Then visit http://localhost:8000
```

## Common Solutions

### Solution 1: Use the Simplified Workflow

I've created a simplified workflow (`maven-site-simple.yml`) that should be more reliable:

- **Single job** instead of separate build/deploy jobs
- **Direct deployment** from `target/site` (not `target/staging`)
- **Simplified permissions**

### Solution 2: Check Repository Permissions

Make sure your repository has the right permissions:

1. **Repository Settings** → **Actions** → **General**
2. **Workflow permissions** should be set to:
   - ✅ "Read and write permissions"
   - ✅ "Allow GitHub Actions to create and approve pull requests"

### Solution 3: Enable GitHub Pages

If GitHub Pages isn't enabled:

1. **Repository Settings** → **Pages**
2. **Source**: Select "GitHub Actions"
3. **Save** the settings

### Solution 4: Check Branch Protection

If you have branch protection rules:

1. **Repository Settings** → **Branches**
2. **Make sure the workflow can push** to the branch
3. **Or temporarily disable** branch protection for testing

## Alternative: Use the Legacy Deployment Method

If the new GitHub Actions deployment doesn't work, you can use the legacy method:

```yaml
name: Maven Site (Legacy)

on:
  push:
    branches: [ main, master ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
    - name: Build site
      run: mvn clean site -DskipTests
    - name: Deploy to GitHub Pages
      uses: peaceiris/actions-gh-pages@v3
      with:
        github_token: ${{ secrets.GITHUB_TOKEN }}
        publish_dir: target/site
        force_orphan: true
```

## Expected Timeline

After fixing the issues:

- **Workflow runs**: Immediately after push
- **Site deployment**: 1-2 minutes after workflow completes
- **Site availability**: 5-10 minutes after deployment
- **DNS propagation**: Up to 24 hours (usually much faster)

## Verification

Once fixed, you should see:

1. **Workflow succeeds** in the Actions tab
2. **Site accessible** at `https://jamesbognar.github.io/bean-centric-testing/`
3. **Javadocs available** at `https://jamesbognar.github.io/bean-centric-testing/apidocs/`

## Still Having Issues?

If you're still getting 404 errors:

1. **Check the workflow logs** for specific error messages
2. **Try the simplified workflow** (`maven-site-simple.yml`)
3. **Verify GitHub Pages is enabled** in repository settings
4. **Check if the site files are being generated** in `target/site/`

The most common issue is that GitHub Pages isn't set to use "GitHub Actions" as the source.
