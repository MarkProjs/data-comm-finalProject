#!/usr/bin/env python3
########################################################################
# Filename    : SenseLED.py
# Description : Control led with infrared Motion sensor.
# Author      : www.freenove.com
# modification: 2019/12/28
########################################################################
import RPi.GPIO as GPIO

ledPin = 12       # define ledPin--> to be changed
sensorPin = 13    # define sensorPin--> to be changed

GPIO.setmode(GPIO.BOARD)        # use PHYSICAL GPIO Numbering
GPIO.setup(ledPin, GPIO.OUT)    # set ledPin to OUTPUT mode
GPIO.setup(sensorPin, GPIO.IN)  # set sensorPin to INPUT mode

if GPIO.input(sensorPin)==GPIO.HIGH:
    GPIO.output(ledPin,GPIO.HIGH) # turn on led
    print ('led turned on >>>')
else :
    GPIO.output(ledPin,GPIO.LOW) # turn off led
    print ('led turned off <<<')

GPIO.cleanup() # Release GPIO resource

