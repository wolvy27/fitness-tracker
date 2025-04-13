#!/usr/bin/env bash

spring init \
--boot-version=3.4.4 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=workouts-service \
--package-name=com.fitnesstracker.workouts \
--groupId=com.fitnesstracker.workout \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
workouts-service

spring init \
--boot-version=3.4.4 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=dailylogs-service \
--package-name=com.fitnesstracker.dailylogs \
--groupId=com.fitnesstracker.dailylog \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
dailylogs-service

spring init \
--boot-version=3.4.4 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=meals-service \
--package-name=com.fitnesstracker.meals \
--groupId=com.fitnesstracker.meal \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
meals-service

spring init \
--boot-version=3.4.4 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=users-service \
--package-name=com.fitnesstracker.users \
--groupId=com.fitnesstracker.user \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
users-service

spring init \
--boot-version=3.4.4 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=api-gateway \
--package-name=com.fitnesstracker.apigateway \
--groupId=com.fitnesstracker.apigateway \
--dependencies=web,webflux,validation,hateoas \
--version=1.0.0-SNAPSHOT \
api-gateway

