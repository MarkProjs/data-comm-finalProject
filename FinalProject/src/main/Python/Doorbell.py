#!/usr/bin/env python3
########################################################################
# Filename    : Doorbell.py
# Description : Make doorbell with buzzer and button
# Author      : www.freenove.com
# modification: 2019/12/28
########################################################################
import RPi.GPIO as GPIO

buzzerPin = 11    # define buzzerPin-->To be changed
buttonPin = 12    # define buttonPin--> To be changed

GPIO.setmode(GPIO.BOARD)        # use PHYSICAL GPIO Numbering
GPIO.setup(buzzerPin, GPIO.OUT)   # set buzzerPin to OUTPUT mode
GPIO.setup(buttonPin, GPIO.IN, pull_up_down=GPIO.PUD_UP)    # set buttonPin to PULL UP INPUT model


if GPIO.input(buttonPin)==GPIO.LOW: # if button is pressed
    GPIO.output(buzzerPin,GPIO.HIGH) # turn on buzzer
    print ('buzzer turned on >>>')
else : # if button is relessed
    GPIO.output(buzzerPin,GPIO.LOW) # turn off buzzer
    print ('buzzer turned off <<<')

GPIO.cleanup()
