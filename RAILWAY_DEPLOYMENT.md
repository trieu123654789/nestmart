# Railway Deployment Guide with Azure SQL Database

## Environment Variables to Set in Railway

After deploying to Railway, you need to set these environment variables in your Railway project settings:

### Database Configuration
```
AZURE_DB_NAME=nestmart
AZURE_DB_USERNAME=your_azure_sql_username
AZURE_DB_PASSWORD=your_azure_sql_password
```

### Optional Configuration
```
PORT=8080
JAVA_OPTS=-Djava.security.egd=file:/dev/./urandom
```

## Deployment Steps

### Option 1: Deploy via GitHub (Recommended)

1. **Push your code to GitHub**:
   ```bash
   git add .
   git commit -m "Configure for Railway deployment with Azure SQL"
   git push origin main
   ```

2. **Deploy on Railway**:
   - Go to [railway.app](https://railway.app)
   - Sign up/login with GitHub
   - Click "New Project"
   - Select "Deploy from GitHub repo"
   - Choose your repository
   - Railway will automatically detect your Dockerfile

3. **Configure Environment Variables**:
   - In Railway dashboard, go to your project
   - Click on "Variables" tab
   - Add the environment variables listed above
   - Replace `your_azure_sql_username` and `your_azure_sql_password` with your actual Azure SQL credentials

4. **Redeploy**:
   - After setting variables, click "Deploy" to restart with new configuration

### Option 2: Alternative Free Hosts

If Railway doesn't work, try these alternatives:

#### Render.com
1. Go to [render.com](https://render.com)
2. Create "Web Service" from GitHub
3. Select Docker environment
4. Set the same environment variables

#### Fly.io
1. Go to [fly.io](https://fly.io)
2. Install fly CLI: `npm install -g flyctl`
3. Run: `fly launch`
4. Set environment variables: `fly secrets set AZURE_DB_USERNAME=your_username`

## Database Setup

Make sure your Azure SQL Database:
1. Has the `nestmart` database created
2. Contains your application tables and data
3. Allows connections from the hosting service IP ranges

## Firewall Rules

Add these IP ranges to your Azure SQL Server firewall:
- Railway: Allow Azure services (0.0.0.0 - 0.0.0.0)
- Or add specific IP ranges provided by your hosting service

## Testing

After deployment, test your application by:
1. Visiting the provided URL
2. Accessing `/nestmart/login.htm`
3. Verifying database connectivity
