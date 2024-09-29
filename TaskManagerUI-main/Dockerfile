FROM nginx:alpine

# Copy the built React application (assuming it's in the 'build' folder)
COPY build /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
